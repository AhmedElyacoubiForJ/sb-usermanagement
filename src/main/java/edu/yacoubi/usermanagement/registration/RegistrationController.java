package edu.yacoubi.usermanagement.registration;

import edu.yacoubi.usermanagement.event.RegistrationCompleteEvent;
import edu.yacoubi.usermanagement.registration.token.ITokenEntityService;
import edu.yacoubi.usermanagement.registration.token.TokenEntity;
import edu.yacoubi.usermanagement.user.IUserService;
import edu.yacoubi.usermanagement.user.User;
import edu.yacoubi.usermanagement.utility.UrlUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static edu.yacoubi.usermanagement.utility.TokenUtility.EXPIRED;
import static edu.yacoubi.usermanagement.utility.TokenUtility.VALID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final IUserService userService;
    private final ApplicationEventPublisher publisher;
    private final ITokenEntityService tokenEntityService;

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
                        UrlUtility.getApplicationUrl(httpServletRequest)
                )
        );
        return "redirect:/registration/registration-form?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<TokenEntity> theToken = tokenEntityService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }
        String validatedToken = tokenEntityService.validateToken(token);
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
