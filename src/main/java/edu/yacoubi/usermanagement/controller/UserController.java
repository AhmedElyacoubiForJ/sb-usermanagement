package edu.yacoubi.usermanagement.controller;

import edu.yacoubi.usermanagement.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService IUserService;
    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", IUserService.getAllUsers());
        return "users";
    }
}
