package com.yash.usermanagement.websocket;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.ServerWebSocket;

@ServerWebSocket("/ws/notifications")
public class NotificationWebSocketServer {
    private final WebSocketBroadcaster broadcaster;

    public NotificationWebSocketServer(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public void onOpen(WebSocketSession session) {
        // Optionally handle new connection
    }

    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        // Optionally handle incoming messages from clients
    }

    public void sendPushNotification(String message) {
        broadcaster.broadcastSync(message);
    }
} 