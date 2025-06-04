package com.yash.usermanagementsystem.service;

public interface EmailService {
    /**
     * Sends an email to the specified recipient
     * @param to The recipient's email address
     * @param subject The email subject
     * @param body The email body
     * @throws Exception if there's an error sending the email
     */
    void sendEmail(String to, String subject, String body) throws Exception;
}