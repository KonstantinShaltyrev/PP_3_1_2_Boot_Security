package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthController(UserValidator userValidator, UserService userService, RoleRepository roleRepository) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.roleRepository = roleRepository;
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
        if (roleRepository.findByRoleName("ROLE_USER").isEmpty()) {
            Role role = new Role();

            role.setRoleName("ROLE_USER");
            roleRepository.save(role);
        }

        Set<Role> set = new HashSet<>();

        set.add(roleRepository.findByRoleName("ROLE_USER").get());
        user.setRoles(set);
        userService.addUser(user);
        return "redirect:/auth/login";
    }
}
