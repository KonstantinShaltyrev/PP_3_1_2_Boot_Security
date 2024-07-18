package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final UserService userService;

    @Autowired
    public AuthController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String openLogInPage() {
        return "auth/signin";
    }

    @GetMapping("/signup")
    public String openRegistration(@ModelAttribute("newUser") User user) {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String performRegistration(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        userService.addUser(user, "ROLE_USER");
        return "redirect:/auth/login";
    }
}
