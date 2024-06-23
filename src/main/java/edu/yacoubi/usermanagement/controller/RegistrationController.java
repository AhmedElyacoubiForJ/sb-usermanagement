package edu.yacoubi.usermanagement.controller;

import edu.yacoubi.usermanagement.controller.dto.RegistrationRequest;
import edu.yacoubi.usermanagement.service.IPasswordResetService;
import edu.yacoubi.usermanagement.service.IConfirmationService;
import edu.yacoubi.usermanagement.model.Confirmation;
import edu.yacoubi.usermanagement.service.IUserService;
import edu.yacoubi.usermanagement.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static edu.yacoubi.usermanagement.constants.TokenStatus.INVALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.VALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.EXPIRED;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final IUserService userService;
    private final IConfirmationService confirmationService;
    private final IPasswordResetService passwordResetService;

    @GetMapping("/form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest request,
            HttpServletRequest httpServletRequest) {
        User user = userService.registerUser(request);
        return "redirect:/registration/form?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<Confirmation> theToken = confirmationService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }
        String validatedToken = confirmationService.validateToken(token);
        return getValidationPath(validatedToken);
    }

    @GetMapping("/forgot-password-request")
    public String forgotPasswordRequestForm() {
        return "/forgot-password/request-form";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordInProcess(Model model, HttpServletRequest httpRequest)  {
        String email = httpRequest.getParameter("email");

        try {
            Optional<User> user = userService.findByEmail(email);
            passwordResetService.makeResetPasswordInProcess(user.get());
        } catch (UsernameNotFoundException ex) {
            return "redirect:/registration/forgot-password-request?not_found";
        }

        return "redirect:/registration/forgot-password-request?success";
    }

    @GetMapping("/password-forgot/verifyEmail")
    public String passwordForgotVerifyEmail(@RequestParam("token") String token, Model model) {

        // look if any password forgot token saved for the user
        String verifiedToken = passwordResetService.verifyToken(token);
        if(verifiedToken.equals(INVALID)) {
            return "redirect:/error?invalid";
        }
        if(verifiedToken.equals(EXPIRED)) {
            return "redirect:/error?expired";
        }
        model.addAttribute("token", token);
        return "/forgot-password/reset-form";
    }

    @PostMapping("/password-forgot/reset")
    public String resetPassword(HttpServletRequest httpRequest) {
        // form parameters
        String theToken = httpRequest.getParameter("token");
        String password = httpRequest.getParameter("password");

        Optional<User> theUser = passwordResetService
                .findUserByPasswordResetToken(theToken);
        if (theUser.isPresent()) {
            passwordResetService.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_password";
        }
        return "redirect:/error?not_found";
    }

    private String getValidationPath(String validatedToken) {
        switch (validatedToken) {
            case EXPIRED:
                return "redirect:/error?expired";
            case VALID:
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }
}
