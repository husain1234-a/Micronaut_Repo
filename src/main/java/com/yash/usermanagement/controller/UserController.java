package com.yash.usermanagement.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yash.usermanagement.dto.CreateUserRequest;
import com.yash.usermanagement.dto.UpdateUserRequest;
import com.yash.usermanagement.dto.UserResponse;
import com.yash.usermanagement.dto.PasswordChangeRequestDTO;
import com.yash.usermanagement.dto.PasswordChangeApprovalDTO;
import com.yash.usermanagement.model.User;
import com.yash.usermanagement.service.UserService;
import com.yash.usermanagement.service.NotificationService;
import com.yash.usermanagement.exception.ResourceNotFoundException;
import com.yash.usermanagement.exception.ValidationException;
import com.yash.usermanagement.exception.DuplicateResourceException;
import com.yash.usermanagement.model.PasswordChangeRequest;
import com.yash.usermanagement.model.PasswordChangeStatus;
import com.yash.usermanagement.model.UserRole;
import com.yash.usermanagement.repository.PasswordChangeRequestRepository;
import jakarta.inject.Named;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller("/api/users")
@Tag(name = "User Management")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final NotificationService emailNotificationService;
    private final NotificationService pushNotificationService;
    private final PasswordChangeRequestRepository passwordChangeRequestRepository;

    public UserController(UserService userService,
            @Named("email") NotificationService emailNotificationService,
            @Named("push") NotificationService pushNotificationService,
            PasswordChangeRequestRepository passwordChangeRequestRepository) {
        this.userService = userService;
        this.emailNotificationService = emailNotificationService;
        this.pushNotificationService = pushNotificationService;
        this.passwordChangeRequestRepository = passwordChangeRequestRepository;
    }

    @Post
    @Operation(summary = "Create a new user")
    @Secured("ADMIN")
    public HttpResponse<UserResponse> createUser(@Body @Valid CreateUserRequest request) {
        LOG.info("Creating new user with role: {}", request.getRole());
        try {
            User user = convertToUser(request);
            User createdUser = userService.createUser(user);

            // Send welcome notification and email
            emailNotificationService.sendUserCreationNotification(
                    createdUser.getId(),
                    createdUser.getEmail(),
                    request.getPassword());

            return HttpResponse.created(convertToUserResponse(createdUser));
        } catch (DuplicateResourceException e) {
            LOG.warn("Duplicate user creation attempted: {}", e.getMessage());
            throw e;
        } catch (ValidationException e) {
            LOG.warn("Invalid user data: {}", e.getMessage());
            throw e;
        }
    }

    @Get
    @Operation(summary = "Get all users")
    @Secured("ADMIN")
    public HttpResponse<List<UserResponse>> getAllUsers() {
        LOG.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        return HttpResponse.ok(userResponses);
    }

    @Get("/{id}")
    @Operation(summary = "Get user by ID")
    @Secured({ "ADMIN", "USER" })
    public HttpResponse<UserResponse> getUserById(@PathVariable UUID id) {
        LOG.info("Fetching user with id: {}", id);
        try {
            User user = userService.getUserById(id);
            return HttpResponse.ok(convertToUserResponse(user));
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found with id: {}", id);
            throw e;
        }
    }

    @Put("/{id}")
    @Operation(summary = "Update user")
    @Secured({ "ADMIN", "USER" })
    public HttpResponse<UserResponse> updateUser(@PathVariable UUID id, @Body @Valid UpdateUserRequest request) {
        try {
            User user = convertToUser(request);
            User updatedUser = userService.updateUser(id, user);
            return HttpResponse.ok(convertToUserResponse(updatedUser));
        } catch (Exception e) {
            LOG.error("Error updating user: {}", e.getMessage());
            throw e;
        }
    }

    @Delete("/{id}")
    @Operation(summary = "Delete user")
    @Secured({ "ADMIN", "USER" })
    public MutableHttpResponse<Map<String, Boolean>> deleteUser(@PathVariable UUID id) {
        LOG.info("Deleting user with id: {}", id);
        try {
            User user = userService.getUserById(id); // Get user before deletion

            // Send deletion notification
            try {
                emailNotificationService.sendAccountDeletionNotification(user.getId(), user.getEmail());
            } catch (Exception e) {
                LOG.error("Failed to send deletion notification email: {}", e.getMessage());
            }
            userService.deleteUser(id);

            return HttpResponse.ok(Collections.singletonMap("success", true));
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found for deletion with id: {}", id);
            throw e;
        }
    }

    @Get("/email/{email}")
    @Operation(summary = "Get user by email")
    @Secured({ "ADMIN", "USER" })
    public HttpResponse<UserResponse> getUserByEmail(@PathVariable String email) {
        LOG.info("Finding user by email: {}", email);
        try {
            return userService.findByEmail(email)
                    .map(this::convertToUserResponse)
                    .map(HttpResponse::ok)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        } catch (ValidationException e) {
            LOG.warn("Invalid email format: {}", email);
            throw e;
        }
    }

    @Post("/{id}/change-password")
    @Operation(summary = "Request password change")
    @Secured("USER")
    public HttpResponse<Void> requestPasswordChange(
            @PathVariable UUID id,
            @Body @Valid PasswordChangeRequestDTO request) {
        LOG.info("Requesting password change for user with id: {}", id);
        try {
            User user = userService.getUserById(id);

            // Validate current password
            if (!userService.validateCurrentPassword(id, request.getOldPassword())) {
                throw new ValidationException("Current password is incorrect");
            }

            // Create password change request
            PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
            passwordChangeRequest.setUserId(id);
            passwordChangeRequest.setNewPassword(request.getNewPassword());
            passwordChangeRequest.setStatus(PasswordChangeStatus.PENDING);
            passwordChangeRequest.setCreatedAt(LocalDateTime.now());
            passwordChangeRequestRepository.save(passwordChangeRequest);

            // Send notification to admin
            try {
                emailNotificationService.sendPasswordResetRequestNotification(user.getId(), user.getEmail());
            } catch (Exception e) {
                LOG.error("Failed to send password reset request email: {}", e.getMessage());
            }

            return HttpResponse.accepted();
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found for password change request with id: {}", id);
            throw e;
        } catch (ValidationException e) {
            LOG.warn("Invalid password change request: {}", e.getMessage());
            throw e;
        }
    }

    @Put("/{id}/approve-password-change")
    @Operation(summary = "Approve password change request")
    @Secured("ADMIN")
    public HttpResponse<Void> approvePasswordChange(
            @PathVariable UUID id,
            @Body @Valid PasswordChangeApprovalDTO request) {
        LOG.info("Processing password change approval for user with id: {}", id);
        try {
            // Verify admin
            User admin = userService.getUserById(request.getAdminId());
            // if (admin.getRole() != UserRole.ADMIN) {
            // throw new ValidationException("Only admin can approve password changes");
            // }

            // Get user and password change request
            User user = userService.getUserById(id);
            PasswordChangeRequest passwordChangeRequest = passwordChangeRequestRepository
                    .findByUserIdAndStatus(id, PasswordChangeStatus.PENDING)
                    .orElseThrow(() -> new ResourceNotFoundException("No pending password change request found"));

            if (request.isApproved()) {
                // Update password
                userService.changePassword(id, passwordChangeRequest.getNewPassword());

                // Update request status and admin ID
                passwordChangeRequest.setStatus(PasswordChangeStatus.APPROVED);
                passwordChangeRequest.setAdminId(request.getAdminId());
                passwordChangeRequest.setUpdatedAt(LocalDateTime.now());
                passwordChangeRequestRepository.update(passwordChangeRequest);

                // Send approval notification
                try {
                    emailNotificationService.sendPasswordResetApprovalNotification(user.getId(), user.getEmail());
                } catch (Exception e) {
                    LOG.error("Failed to send password reset approval email: {}", e.getMessage());
                }
            } else {
                // Reject password change
                userService.rejectPasswordChange(id, request.getAdminId());

                // Update request status and admin ID
                passwordChangeRequest.setStatus(PasswordChangeStatus.REJECTED);
                passwordChangeRequest.setAdminId(request.getAdminId());
                passwordChangeRequest.setUpdatedAt(LocalDateTime.now());
                passwordChangeRequestRepository.update(passwordChangeRequest);

                // Send rejection notification
                try {
                    emailNotificationService.sendPasswordChangeRejectionNotification(user.getId(), user.getEmail());
                } catch (Exception e) {
                    LOG.error("Failed to send password change rejection email: {}", e.getMessage());
                }
            }

            return HttpResponse.ok();
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found for password change approval with id: {}", id);
            throw e;
        }
    }

    @Get("/password-change-requests/pending")
    @Secured("ADMIN")
    @Operation(summary = "Get all pending password change requests")
    public HttpResponse<List<Map<String, Object>>> getAllPendingPasswordChangeRequests() {
        List<PasswordChangeRequest> pendingRequests = userService.getPendingPasswordChangeRequests();
        // For each request, fetch user info for display
        List<Map<String, Object>> result = pendingRequests.stream().map(req -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", req.getId());
            map.put("userId", req.getUserId());
            map.put("newPassword", req.getNewPassword());
            map.put("status", req.getStatus());
            map.put("adminId", req.getAdminId());
            map.put("createdAt", req.getCreatedAt());
            map.put("updatedAt", req.getUpdatedAt());
            // Fetch user info
            try {
                User user = userService.getUserById(req.getUserId());
                map.put("userFirstName", user.getFirstName());
                map.put("userLastName", user.getLastName());
                map.put("userEmail", user.getEmail());
            } catch (Exception e) {
                // User might have been deleted
                map.put("userFirstName", "");
                map.put("userLastName", "");
                map.put("userEmail", "");
            }
            return map;
        }).collect(Collectors.toList());
        return HttpResponse.ok(result);
    }

    @Put("/password-change-requests/{requestId}/approve")
    @Secured("ADMIN")
    @Operation(summary = "Approve or reject a password change request")
    public HttpResponse<Void> approveOrRejectPasswordChangeRequest(
            @PathVariable UUID requestId,
            @Body @Valid PasswordChangeApprovalDTO approvalDTO) {
        // Find the request
        PasswordChangeRequest req = passwordChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Password change request not found"));
        if (approvalDTO.isApproved()) {
            // Approve: change password, set status, set adminId, set updatedAt
            userService.changePassword(req.getUserId(), req.getNewPassword());
            req.setStatus(PasswordChangeStatus.APPROVED);
            req.setAdminId(approvalDTO.getAdminId());
            req.setUpdatedAt(LocalDateTime.now());
            passwordChangeRequestRepository.update(req);
            // Send approval notification
            try {
                User user = userService.getUserById(req.getUserId());
                emailNotificationService.sendPasswordResetApprovalNotification(user.getId(), user.getEmail());
            } catch (Exception e) {
                LOG.error("Failed to send password reset approval notification: {}", e.getMessage());
            }
        } else {
            // Reject: set status, set adminId, set updatedAt
            req.setStatus(PasswordChangeStatus.REJECTED);
            req.setAdminId(approvalDTO.getAdminId());
            req.setUpdatedAt(LocalDateTime.now());
            passwordChangeRequestRepository.update(req);
            // Send rejection notification
            try {
                User user = userService.getUserById(req.getUserId());
                emailNotificationService.sendPasswordChangeRejectionNotification(user.getId(), user.getEmail());
            } catch (Exception e) {
                LOG.error("Failed to send password change rejection notification: {}", e.getMessage());
            }
        }
        return HttpResponse.ok();
    }

    // Helper methods for conversion
    private User convertToUser(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setRole(request.getRole());
        user.setAddress(request.getAddress());
        return user;
    }

    private User convertToUser(UpdateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setRole(request.getRole());
        user.setAddress(request.getAddress());
        return user;
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setAddress(user.getAddress());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setGender(user.getGender());
        response.setRole(user.getRole());
        return response;
    }
}