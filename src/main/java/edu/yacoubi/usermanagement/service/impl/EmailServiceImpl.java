package edu.yacoubi.usermanagement.service.impl;

import edu.yacoubi.usermanagement.security.filter.ClientTypeHolder;
import edu.yacoubi.usermanagement.service.IEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import static edu.yacoubi.usermanagement.utility.EmailUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
@RequestScope
public class EmailServiceImpl implements IEmailService {
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

        log.info("EmailSender to confirm registration");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setText(getEmailMessage(name, host, token, clientTypeHolder));
            mailSender.send(message);
            log.info("Email successfully send, {}", message);
        } catch (Exception e) {
            String errorMessage = "Unable to send email";
            log.error("Error occurred sending email, {} \n {}", errorMessage , e.getMessage());
            throw new RuntimeException(errorMessage);
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
            message.setText(getResetPasswordRequestMessage(name, host, token));
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

    /*
    * private void sentVerificationEmail(String url)
            throws MessagingException, UnsupportedEncodingException {
        log.info("sentVerificationEmail");
        log.info(url);
        //1. build the email
        String subject = "Email Verification";
        String senderName = "Users Verification Service";
        String mailContent = "<p> Hi, " + user.getFirstName() + " " + user.getLastName() + ", </p>" +
                "<p>Thank you for registering with us, Please click on the following link to complete your registration. </p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                "<p>Thank you,</p>" +
                "<p>Users Registration Portal Service</p>";
        //2. send the email
        emailMessage(subject, senderName, mailContent, mailSender, user);
        log.info("sentVerificationEmail");
        log.info(url);
    }

    public void sentPasswordResetVerificationEmail(String url)
            throws MessagingException, UnsupportedEncodingException {
        log.info("sentPasswordResetVerificationEmail");
        log.info(url);
        //1. build the email
        String subject = "Password Reset Request Verification";
        String senderName = "Users Verification Service";
        String mailContent = "<p> Hi, " + "user.getFirstName()" + " " + "user.getLastName()" + ", </p>" +
                "<p><b>You recently requested to reset reset your password,</b>" +
                "Please, follow the link below to complete the action. </p>" +
                "<a href=\"" + url + "\">Reset password</a>" +
                "<p>Thank you,</p>" +
                "<p>Users Registration Portal Service</p>";
        //2. send the email
        emailMessage(subject, senderName, mailContent, mailSender, user);
        log.info("sentPasswordResetVerificationEmail, {}", url);
    }

    private void emailMessage(
            String subject,
            String senderName,
            String mailContent,
            JavaMailSender mailSender,
            User theUser)
        throws MessagingException, UnsupportedEncodingException {
       MimeMessage message = mailSender.createMimeMessage();
       var messageHelper = new MimeMessageHelper(message);
       messageHelper.setFrom("a.el_yacoubi@gmx.de", senderName);
       messageHelper.setTo(theUser.getEmail());
       messageHelper.setSubject(subject);
       messageHelper.setText(mailContent, true);
       mailSender.send(message);
    }
    *
    * */
}
