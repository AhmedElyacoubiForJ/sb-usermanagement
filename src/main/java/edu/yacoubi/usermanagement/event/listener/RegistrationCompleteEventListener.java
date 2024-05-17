package edu.yacoubi.usermanagement.event.listener;

import edu.yacoubi.usermanagement.event.RegistrationCompleteEvent;
import edu.yacoubi.usermanagement.registration.token.ITokenEntityService;
import edu.yacoubi.usermanagement.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener
        implements ApplicationListener<RegistrationCompleteEvent> {
    private final ITokenEntityService tokenService;
    private final JavaMailSender mailSender;
    private User user;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        log.info("onApplicationEvent");
        log.info(event.getConfirmationUrl());

        //1. get the user
        user = event.getUser();
        //2. generate a token for the user
        String generatedToken = UUID.randomUUID().toString();
        //3. save the token for the use
        tokenService.saveTokenForUser(generatedToken, user);
        //4. build the verification email
        String url = event.getConfirmationUrl() +
                "/registration/verifyEmail?token=" +
                generatedToken;
        //5. send the email for the user
        try {
            sentVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("onApplicationEvent", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void sentVerificationEmail(String url)
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
}
