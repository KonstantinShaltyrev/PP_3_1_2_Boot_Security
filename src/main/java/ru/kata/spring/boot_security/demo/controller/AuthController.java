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
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AuthController(UserValidator userValidator, UserService userService, RoleService roleService) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.roleService = roleService;
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
        if (roleService.findByRoleName("ROLE_USER").isEmpty()) {
            Role role = new Role();

            role.setRoleName("ROLE_USER");
            roleService.save(role);
        }

        Set<Role> set = new HashSet<>();

        set.add(roleService.findByRoleName("ROLE_USER").get());
        user.setRoles(set);
        userService.addUser(user);
        return "redirect:/auth/login";
    }
}
