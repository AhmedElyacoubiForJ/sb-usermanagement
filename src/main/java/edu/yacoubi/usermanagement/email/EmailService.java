package edu.yacoubi.usermanagement.email;

public interface EmailService {
    void sendNewAccountEmail(String name, String toEmail, String token);
    void sendPasswordResetEmail(String name, String toEmail, String token);


    void sendSimpleEmail(String name, String to, String subject, String message);
    void sendHtmlEmail(String name, String to, String token);
}
