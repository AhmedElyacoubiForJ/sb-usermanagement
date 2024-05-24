package edu.yacoubi.usermanagement.email;

public interface EmailService {
    void sendSimpleEmail(String name, String to, String subject, String message);
    void sendHtmlEmail(String name, String to, String token);
}
