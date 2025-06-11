package com.yash.usermanagement.service.impl;

import com.yash.usermanagement.config.EmailConfig;
import com.yash.usermanagement.exception.ResourceNotFoundException;
import com.yash.usermanagement.model.Notification;
import com.yash.usermanagement.model.NotificationPriority;
import com.yash.usermanagement.model.User;
import com.yash.usermanagement.repository.NotificationRepository;
import com.yash.usermanagement.repository.UserRepository;
import com.yash.usermanagement.service.NotificationService;
import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.email.MultipartBody;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailSender<?, ?> emailSender;
    private final EmailConfig emailConfig;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
            UserRepository userRepository,
            EmailSender<?, ?> emailSender,
            EmailConfig emailConfig) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.emailConfig = emailConfig;
    }

    @Override
    public Notification createNotification(Notification notification) {
        // Validate user exists
        User user = userRepository.findById(notification.getUserId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with id: " + notification.getUserId()));

        notification.setId(UUID.randomUUID().toString());
        notification.setRead(false);
        notification.setCreatedAt(java.time.LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllNotifications() {
        log.info("Fetching all notifications");
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> getNotificationById(String id) {
        log.info("Fetching notification with id: {}", id);
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> getNotificationsByUserId(UUID userId) {
        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        log.info("Fetching notifications for user: {}", userId);
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getNotificationsByUserIdAndPriority(UUID userId, NotificationPriority priority) {
        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        log.info("Fetching {} priority notifications for user: {}", priority, userId);
        return notificationRepository.findByUserIdAndPriority(userId, priority);
    }

    @Override
    public void deleteNotification(String id) {
        log.info("Deleting notification with id: {}", id);
        notificationRepository.findById(id).ifPresent(notificationRepository::delete);
    }

    @Override
    public void sendUserCreationNotification(UUID userId, String email, String password) {
        log.info("Sending user creation notification to: {}", email);
        try {
            // Validate user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            // Use email from user object
            String userEmail = user.getEmail();

            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setTitle("Welcome to User Management System");
            notification.setMessage("Your account has been created successfully.");
            notification.setPriority(NotificationPriority.HIGH);
            notification.setRead(false);
            notification.setCreatedAt(java.time.LocalDateTime.now());
            notificationRepository.save(notification);

            // Send welcome email
            Email.Builder welcomeEmailBuilder = Email.builder()
                    .from(emailConfig.getFrom())
                    .to(userEmail)
                    .subject("Welcome to User Management System")
                    .body(new MultipartBody(
                            "Welcome to User Management System!\n\n" +
                                    "Your account has been created successfully.\n" +
                                    "Your temporary password is: " + password + "\n\n" +
                                    "Please change your password after first login.",
                            "Welcome to User Management System!<br><br>" +
                                    "Your account has been created successfully.<br>" +
                                    "Your temporary password is: " + password + "<br><br>" +
                                    "Please change your password after first login."));

            emailSender.send(welcomeEmailBuilder);
            log.info("Welcome email sent successfully to: {}", userEmail);
        } catch (Exception e) {
            log.error("Error in sendUserCreationNotification for user: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void sendPasswordResetRequestNotification(UUID userId, String email) {
        log.info("Sending password reset request notification for user: {}", userId);
        try {
            // Validate user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            // Use email from user object
            String userEmail = user.getEmail();

            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setTitle("Password Reset Request");
            notification.setMessage("A password reset has been requested for your account.");
            notification.setPriority(NotificationPriority.HIGH);
            notification.setRead(false);
            notification.setCreatedAt(java.time.LocalDateTime.now());
            notificationRepository.save(notification);

            // Send password reset request email to admin
            Email.Builder resetRequestEmailBuilder = Email.builder()
                    .from(emailConfig.getFrom())
                    .to(emailConfig.getFrom())
                    .subject("Password Reset Request")
                    .body(new MultipartBody(
                            "A password reset has been requested for user: " + userEmail,
                            "A password reset has been requested for user: " + userEmail));

            emailSender.send(resetRequestEmailBuilder);
            log.info("Password reset request email sent successfully to admin");
        } catch (Exception e) {
            log.error("Error in sendPasswordResetRequestNotification for user: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void sendPasswordResetApprovalNotification(UUID userId, String email) {
        log.info("Sending password reset approval notification for user: {}", userId);
        try {
            // Validate user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            // Use email from user object
            String userEmail = user.getEmail();

            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setTitle("Password Reset Approved");
            notification.setMessage("Your password reset request has been approved.");
            notification.setPriority(NotificationPriority.HIGH);
            notification.setRead(false);
            notification.setCreatedAt(java.time.LocalDateTime.now());
            notificationRepository.save(notification);

            // Send password reset approval email
            Email.Builder resetApprovalEmailBuilder = Email.builder()
                    .from(emailConfig.getFrom())
                    .to(userEmail)
                    .subject("Password Reset Approved")
                    .body(new MultipartBody(
                            "Your password reset request has been approved.\n" +
                                    "Please use the link below to reset your password.",
                            "Your password reset request has been approved.<br>" +
                                    "Please use the link below to reset your password."));

            emailSender.send(resetApprovalEmailBuilder);
            log.info("Password reset approval email sent successfully to: {}", userEmail);
        } catch (Exception e) {
            log.error("Error in sendPasswordResetApprovalNotification for user: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void sendPasswordChangeNotification(UUID userId, String email) {
        log.info("Sending password change notification for user: {}", userId);
        try {
            // Validate user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            // Use email from user object
            String userEmail = user.getEmail();

            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setTitle("Password Changed");
            notification.setMessage("Your password has been changed successfully.");
            notification.setPriority(NotificationPriority.HIGH);
            notification.setRead(false);
            notification.setCreatedAt(java.time.LocalDateTime.now());
            notificationRepository.save(notification);

            // Send password change email
            Email.Builder passwordChangeEmailBuilder = Email.builder()
                    .from(emailConfig.getFrom())
                    .to(userEmail)
                    .subject("Password Changed")
                    .body(new MultipartBody(
                            "Your password has been changed successfully.\n" +
                                    "If you did not make this change, please contact support immediately.",
                            "Your password has been changed successfully.<br>" +
                                    "If you did not make this change, please contact support immediately."));

            emailSender.send(passwordChangeEmailBuilder);
            log.info("Password change email sent successfully to: {}", userEmail);
        } catch (Exception e) {
            log.error("Error in sendPasswordChangeNotification for user: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void broadcastNotification(String title, String message, NotificationPriority priority) {
        log.info("Broadcasting notification: {}", title);
        try {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                Notification notification = new Notification();
                notification.setUserId(user.getId());
                notification.setTitle(title);
                notification.setMessage(message);
                notification.setPriority(priority);
                notification.setRead(false);
                notification.setCreatedAt(java.time.LocalDateTime.now());
                notificationRepository.save(notification);

                // Send broadcast email
                Email.Builder broadcastEmailBuilder = Email.builder()
                        .from(emailConfig.getFrom())
                        .to(user.getEmail())
                        .subject(title)
                        .body(new MultipartBody(message, message));

                emailSender.send(broadcastEmailBuilder);
                log.info("Broadcast email sent successfully to: {}", user.getEmail());
            }
        } catch (Exception e) {
            log.error("Error in broadcastNotification", e);
            throw e;
        }
    }
}