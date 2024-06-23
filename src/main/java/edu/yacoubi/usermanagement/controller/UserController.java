package edu.yacoubi.usermanagement.controller;

import edu.yacoubi.usermanagement.model.User;
import edu.yacoubi.usermanagement.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, @PathVariable("id") Long id) {
        Optional<User> userOptional = userService.findById(id);
        model.addAttribute("user", userOptional.get());
        return "user-update-form";
    }

    @PostMapping("/update/{id}")
    public String updateUser(User user, @PathVariable("id") Long id) {
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
        return "redirect:/users?update_success";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users?delete_success";
    }
}
