package edu.yacoubi.usermanagement.service.impl;

import edu.yacoubi.usermanagement.config.ClientTypeHolder;
import edu.yacoubi.usermanagement.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import static edu.yacoubi.usermanagement.utility.EmailUtils.getEmailMessage;
import static edu.yacoubi.usermanagement.utility.EmailUtils.getResetPasswordMessage;

@Service
@RequiredArgsConstructor
@Slf4j
@RequestScope
public class EmailServiceImpl implements EmailService {
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account verification";
    private static final String PASSWORD_REST_REQUEST = "Reset Password Request";
    private final JavaMailSender mailSender;
    private final ClientTypeHolder clientTypeHolder;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async // asynchronous means run in a background thread
    public void sendNewAccountEmail(String name, String toEmail, String token) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setText(getEmailMessage(name, host, token, clientTypeHolder));
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    @Async // asynchronous means run in a background thread
    public void sendPasswordResetEmail(String name, String toEmail, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(PASSWORD_REST_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setText(getResetPasswordMessage(name, host, token));
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    @Async
    public void sendSimpleEmail(String name, String toEmail, String subject, String message) {
        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String toEmail, String token) {
        // TODO: implement
        throw new  UnsupportedOperationException("Not implemented");
    }
}
