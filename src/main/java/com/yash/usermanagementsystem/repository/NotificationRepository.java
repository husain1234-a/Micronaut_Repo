package com.yash.usermanagementsystem.repository;

import com.yash.usermanagementsystem.model.Notification;
import com.yash.usermanagementsystem.model.NotificationPriority;
import io.micronaut.data.annotation.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NotificationRepository {
    private final DynamoDbTable<Notification> notificationTable;

    @Inject
    public NotificationRepository(DynamoDbEnhancedClient enhancedClient) {
        this.notificationTable = enhancedClient.table("notifications", TableSchema.fromBean(Notification.class));
    }

    public Notification save(Notification notification) {
        notificationTable.putItem(notification);
        return notification;
    }

    public Optional<Notification> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Optional.ofNullable(notificationTable.getItem(key));
    }

    public List<Notification> findByUserId(String userId) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(userId).build());

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();

        return notificationTable.query(queryRequest)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<Notification> findByUserIdAndPriority(String userId, NotificationPriority priority) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(userId).build());

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();

        return notificationTable.query(queryRequest)
                .items()
                .stream()
                .filter(notification -> notification.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public void delete(Notification notification) {
        Key key = Key.builder().partitionValue(notification.getId()).build();
        notificationTable.deleteItem(key);
    }

    public List<Notification> findAll() {
        return notificationTable.scan()
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<Notification> findByPriority(NotificationPriority priority) {
        return notificationTable.scan()
                .items()
                .stream()
                .filter(notification -> notification.getPriority() == priority)
                .collect(Collectors.toList());
    }
} 