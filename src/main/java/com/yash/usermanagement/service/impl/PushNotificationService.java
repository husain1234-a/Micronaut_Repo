package com.yash.usermanagement.service.impl;

import com.yash.usermanagement.model.Notification;
import com.yash.usermanagement.model.NotificationPriority;
import com.yash.usermanagement.service.NotificationService;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.UUID;

@Named("push")
public class PushNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    @Inject
    FcmService fcmService;

    @Override
    public Notification createNotification(Notification notification) {
        log.info("PUSH: createNotification called (not implemented)");
        return notification;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Notification> getNotificationById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Notification> getNotificationsByUserId(UUID userId) {
        return Collections.emptyList();
    }

    @Override
    public List<Notification> getNotificationsByUserIdAndPriority(UUID userId, NotificationPriority priority) {
        return Collections.emptyList();
    }

    @Override
    public void deleteNotification(String id) {
        log.info("PUSH: deleteNotification called (not implemented)");
    }

    @Override
    public void sendUserCreationNotification(UUID userId, String email, String password) {
        log.info("PUSH: sendUserCreationNotification called (not implemented)");
    }

    @Override
    public void sendPasswordResetRequestNotification(UUID userId, String email) {
        log.info("PUSH: sendPasswordResetRequestNotification called (not implemented)");
    }

    @Override
    public void sendPasswordResetApprovalNotification(UUID userId, String email) {
        log.info("PUSH: sendPasswordResetApprovalNotification called (not implemented)");
    }

    @Override
    public void sendPasswordChangeNotification(UUID userId, String email) {
        log.info("PUSH: sendPasswordChangeNotification called (not implemented)");
    }

    @Override
    public void sendPasswordChangeRejectionNotification(UUID userId, String email) {
        log.info("PUSH: sendPasswordChangeRejectionNotification called (not implemented)");
    }

    @Override
    public void broadcastNotification(String title, String message, NotificationPriority priority) {
        // TODO: Get all FCM tokens from DB and send notification to each
        List<String> fcmTokens = getAllFcmTokens(); // Implement this method to fetch tokens from DB
        for (String token : fcmTokens) {
            try {
                fcmService.sendPush(token, title, message);
            } catch (Exception e) {
                log.error("Failed to send FCM push notification to token {}: {}", token, e.getMessage());
            }
        }
    }

    @Override
    public void sendAccountDeletionNotification(UUID userId, String email) {
        log.info("PUSH: sendAccountDeletionNotification called (not implemented)");
    }

    @Override
    public void markNotificationAsRead(String id) {
        log.info("PUSH: markNotificationAsRead called (not implemented)");
    }

    // Placeholder: Implement this to fetch all FCM tokens from your DB
    private List<String> getAllFcmTokens() {
        // TODO: Query your user/device table for all FCM tokens
        return Collections.emptyList();
    }
}