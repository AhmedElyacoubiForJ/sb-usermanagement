package edu.yacoubi.usermanagement.email;

public interface EmailService {
    void sendHtmlEmail(String name, String to, String token);
}
