package com.yash.usermanagementsystem.service.impl;

import com.yash.usermanagementsystem.service.EmailService;
import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class EmailServiceImpl implements EmailService {

    @Inject
    private EmailSender emailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        // Create Email using the static of() method or constructor
        Email email = Email.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .build();

        emailSender.send(email);
    }
}