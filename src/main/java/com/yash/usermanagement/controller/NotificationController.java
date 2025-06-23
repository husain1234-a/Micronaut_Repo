package com.yash.usermanagement.controller;

import com.yash.usermanagement.model.Notification;
import com.yash.usermanagement.model.NotificationPriority;
import com.yash.usermanagement.service.NotificationService;
import com.yash.usermanagement.dto.BroadcastNotificationRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.scheduling.annotation.ExecuteOn;
import com.yash.usermanagement.dto.AIGenerateRequest;
import com.yash.usermanagement.dto.AIGenerateResponse;
import com.yash.usermanagement.service.GeminiService;

import java.util.List;
import java.util.UUID;

@Controller("/api/notifications")
@Tag(name = "Notification Management")
public class NotificationController {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final GeminiService geminiService;

    public NotificationController(NotificationService notificationService, GeminiService geminiService) {
        this.notificationService = notificationService;
        this.geminiService = geminiService;
    }

    @Post
    @Operation(summary = "Create a new notification")
    public HttpResponse<Notification> createNotification(@Body @Valid Notification notification) {
        LOG.info("Creating new notification");
        return HttpResponse.created(notificationService.createNotification(notification));
    }

    @Get
    @Operation(summary = "Get all notifications")
    public HttpResponse<List<Notification>> getAllNotifications() {
        LOG.info("Fetching all notifications");
        return HttpResponse.ok(notificationService.getAllNotifications());
    }

    @Get("/{id}")
    @Operation(summary = "Get notification by ID")
    public HttpResponse<Notification> getNotificationById(@PathVariable String id) {
        LOG.info("Fetching notification with id: {}", id);
        return notificationService.getNotificationById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Get("/user/{userId}")
    @Operation(summary = "Get notifications by user ID")
    public HttpResponse<List<Notification>> getNotificationsByUserId(@PathVariable UUID userId) {
        LOG.info("Fetching notifications for user: {}", userId);
        return HttpResponse.ok(notificationService.getNotificationsByUserId(userId));
    }

    @Get("/user/{userId}/priority/{priority}")
    @Operation(summary = "Get notifications by user ID and priority")
    public HttpResponse<List<Notification>> getNotificationsByUserIdAndPriority(
            @PathVariable UUID userId,
            @PathVariable NotificationPriority priority) {
        LOG.info("Fetching {} priority notifications for user: {}", priority, userId);
        return HttpResponse.ok(notificationService.getNotificationsByUserIdAndPriority(userId, priority));
    }

    @Patch("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public HttpResponse<Void> markNotificationAsRead(@PathVariable String id) {
        LOG.info("Marking notification as read: {}", id);
        notificationService.markNotificationAsRead(id);
        return HttpResponse.noContent();
    }

    @Delete("/{id}")
    @Operation(summary = "Delete notification")
    public HttpResponse<Void> deleteNotification(@PathVariable String id) {
        LOG.info("Deleting notification with id: {}", id);
        notificationService.deleteNotification(id);
        return HttpResponse.noContent();
    }

    @Post("/ai-generate")
    @Operation(summary = "Generate message using AI")
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<AIGenerateResponse> generateAIMessage(@Body @Valid AIGenerateRequest request) {
        LOG.info("Generating AI message with prompt: {}", request.getPrompt());
        String generatedMessage = geminiService.generateMessage(request.getPrompt());
        return HttpResponse.ok(new AIGenerateResponse(generatedMessage));
    }

    @Post("/broadcast")
    @Operation(summary = "Broadcast notification to all users")
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<Void> broadcastNotification(@Body @Valid BroadcastNotificationRequest request) {
        LOG.info("Broadcasting notification: {}", request.getTitle());
        notificationService.broadcastNotification(
                request.getTitle(),
                request.getMessage(),
                request.getPriority()
                );
                // request.isUseAI(),
                // request.getAiPrompt()
        return HttpResponse.accepted();
    }

    @Post("/test/welcome")
    @Operation(summary = "Test welcome notification")
    public void testWelcomeNotification(@Body TestNotificationRequest request) {
        notificationService.sendUserCreationNotification(
                request.getUserId(),
                request.getEmail(),
                request.getPassword());
    }

    @Post("/test/reset-request")
    @Operation(summary = "Test password reset request notification")
    public void testResetRequestNotification(@Body TestNotificationRequest request) {
        notificationService.sendPasswordResetRequestNotification(
                request.getUserId(),
                request.getEmail());
    }

    @Post("/test/reset-approval")
    @Operation(summary = "Test password reset approval notification")
    public void testResetApprovalNotification(@Body TestNotificationRequest request) {
        notificationService.sendPasswordResetApprovalNotification(
                request.getUserId(),
                request.getEmail());
    }

    @Post("/test/password-change")
    @Operation(summary = "Test password change notification")
    public void testPasswordChangeNotification(@Body TestNotificationRequest request) {
        notificationService.sendPasswordChangeNotification(
                request.getUserId(),
                request.getEmail());
    }

    @Post("/test/broadcast")
    @Operation(summary = "Test broadcast notification")
    public void testBroadcastNotification(@Body BroadcastNotificationRequest request) {
        notificationService.broadcastNotification(
                request.getTitle(),
                request.getMessage(),
                request.getPriority());
    }
}

@Serdeable
class TestNotificationRequest {
    private UUID userId;
    private String email;
    private String password;

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}