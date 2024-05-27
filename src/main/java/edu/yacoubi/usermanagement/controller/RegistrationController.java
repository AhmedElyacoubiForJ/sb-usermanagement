package edu.yacoubi.usermanagement.controller;

import edu.yacoubi.usermanagement.controller.dto.RegistrationRequest;
import edu.yacoubi.usermanagement.event.RegistrationCompleteEvent;
import edu.yacoubi.usermanagement.event.listener.RegistrationCompleteEventListener;
import edu.yacoubi.usermanagement.service.IPasswordResetTokenService;
import edu.yacoubi.usermanagement.service.ConfirmationService;
import edu.yacoubi.usermanagement.model.Confirmation;
import edu.yacoubi.usermanagement.service.UserService;
import edu.yacoubi.usermanagement.model.User;
import edu.yacoubi.usermanagement.utility.UrlUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

import static edu.yacoubi.usermanagement.utility.ConfirmationUtils.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final ConfirmationService confirmationService;
    private final IPasswordResetTokenService passwordResetTokenService;
    private final RegistrationCompleteEventListener eventListener;

    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") RegistrationRequest request,
            HttpServletRequest httpServletRequest) {
        User user = userService.registerUser(request);

        publisher.publishEvent(
                new RegistrationCompleteEvent(
                        user,
                        UrlUtils.getApplicationUrl(httpServletRequest)
                )
        );
        return "redirect:/registration/registration-form?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<Confirmation> theToken = confirmationService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }
        String validatedToken = confirmationService.validateToken(token);
        switch (validatedToken) {
            case EXPIRED:
                return "redirect:/error?expired";
            case VALID:
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }
    @GetMapping("/forgot-password-request")
    public String forgotPassword() {
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String requestPasswordRequest(Model model, HttpServletRequest httpRequest)  {
        // form parameters
        String email = httpRequest.getParameter("email");
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return "redirect:/registration/forgot-password-request?not_found";
        }

        String passwordResetToken = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
        // send password reset verification to the user
        String url = UrlUtils.getApplicationUrl(httpRequest) +
                "/registration/password-reset-form?token=" +
                passwordResetToken;
        try {


            eventListener.sentPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/registration/forgot-password-request?success";
    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "password-reset-form";
    }

    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest httpRequest) {
        // form parameters
        String theToken = httpRequest.getParameter("token");
        String password = httpRequest.getParameter("password");
        // token has validation time
        String validatedToken = passwordResetTokenService
                .validatePasswordResetToken(theToken);

        if(validatedToken.equals(INVALID)) {
            return "redirect:/error?invalid";
        }
        if(validatedToken.equals(EXPIRED)) {
            return "redirect:/error?expired";
        }
        Optional<User> theUser = passwordResetTokenService
                .findUserByPasswordResetToken(theToken);
        if (theUser.isPresent()) {
            passwordResetTokenService.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_password";
        }
        return "redirect:/error?not_found";
    }
}
