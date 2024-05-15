package edu.yacoubi.usermanagement.registration;

import edu.yacoubi.usermanagement.event.RegistrationCompleteEvent;
import edu.yacoubi.usermanagement.user.IUserService;
import edu.yacoubi.usermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final IUserService userService;
    private final ApplicationEventPublisher publisher;

    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user-request", new RegistrationRequest());
        return "registration";
    }

    // method to register a user
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest request) {
        User user = userService.registerUser(request);
        publisher.publishEvent(new RegistrationCompleteEvent(user, ""));
        return "redirect:/registration/form?success=true";
    }
}
