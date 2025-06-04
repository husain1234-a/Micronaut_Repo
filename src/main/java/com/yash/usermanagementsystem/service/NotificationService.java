package com.yash.usermanagementsystem.service;

import com.yash.usermanagementsystem.dto.NotificationDTO;
import com.yash.usermanagementsystem.model.NotificationPriority;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    /**
     * Create a new notification
     * @param notificationDTO The notification data
     * @return The created notification
     */
    NotificationDTO createNotification(NotificationDTO notificationDTO);

    /**
     * Get a notification by its ID
     * @param id The notification ID
     * @return Optional containing the notification if found
     */
    Optional<NotificationDTO> getNotificationById(String id);

    /**
     * Get all notifications for a specific user
     * @param userId The user ID
     * @return List of notifications for the user
     */
    List<NotificationDTO> getNotificationsByUserId(String userId);

    /**
     * Get notifications by priority for a specific user
     * @param userId The user ID
     * @param priority The notification priority
     * @return List of notifications matching the criteria
     */
    List<NotificationDTO> getNotificationsByPriority(String userId, NotificationPriority priority);

    /**
     * Update an existing notification
     * @param id The notification ID
     * @param notificationDTO The updated notification data
     * @return The updated notification
     */
    NotificationDTO updateNotification(String id, NotificationDTO notificationDTO);

    /**
     * Delete a notification
     * @param id The notification ID
     * @return true if deleted successfully
     */
    boolean deleteNotification(String id);

    /**
     * Mark a notification as read
     * @param id The notification ID
     * @return The updated notification
     */
    NotificationDTO markAsRead(String id);
} 