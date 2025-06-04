package com.yash.usermanagementsystem.service.impl;

import com.yash.usermanagementsystem.dto.NotificationDTO;
import com.yash.usermanagementsystem.model.Notification;
import com.yash.usermanagementsystem.model.NotificationPriority;
import com.yash.usermanagementsystem.repository.NotificationRepository;
import com.yash.usermanagementsystem.service.NotificationService;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return convertToDTO(notification);
    }

    @Override
    public Optional<NotificationDTO> getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByPriority(String userId, NotificationPriority priority) {
        return notificationRepository.findByUserIdAndPriority(userId, priority)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationDTO updateNotification(String id, NotificationDTO notificationDTO) {
        return notificationRepository.findById(id)
                .map(existingNotification -> {
                    updateEntityFromDTO(existingNotification, notificationDTO);
                    return convertToDTO(notificationRepository.save(existingNotification));
                })
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }

    @Override
    @Transactional
    public boolean deleteNotification(String id) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notificationRepository.delete(notification);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public NotificationDTO markAsRead(String id) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setRead(true);
                    return convertToDTO(notificationRepository.save(notification));
                })
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }

    private Notification convertToEntity(@NonNull NotificationDTO dto) {
        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setUserId(dto.getUserId());
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : Instant.now());
        notification.setPriority(dto.getPriority());
        notification.setRead(dto.isRead());
        return notification;
    }

    private NotificationDTO convertToDTO(@NonNull Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setTimestamp(notification.getTimestamp());
        dto.setPriority(notification.getPriority());
        dto.setRead(notification.isRead());
        return dto;
    }

    private void updateEntityFromDTO(@NonNull Notification notification, @NonNull NotificationDTO dto) {
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setPriority(dto.getPriority());
    }
} 