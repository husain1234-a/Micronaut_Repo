package com.yash.usermanagement.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import java.io.FileInputStream;

@Singleton
public class FcmService {
    @PostConstruct
    public void init() throws Exception {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-service-account.json");
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    public String sendPush(String fcmToken, String title, String body) throws Exception {
        Message message = Message.builder()
            .setToken(fcmToken)
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .build();
        return FirebaseMessaging.getInstance().send(message);
    }
} 