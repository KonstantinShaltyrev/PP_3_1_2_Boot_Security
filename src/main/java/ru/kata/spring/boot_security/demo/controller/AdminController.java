package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserValidator userValidator;
    private final UserService userService;

    @Autowired
    public AdminController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping("/users")
    public String showAdminPage(ModelMap model) {
        model.addAttribute("userList", userService.findAll());
        return "admin/users_list";
    }

    @GetMapping(value = "/create")
    public String createForm(Model model) {
        model.addAttribute("admCreateUser", new User());
        model.addAttribute("roleName", "ROLE_USER");
        return "admin/user_create";
    }

    @SuppressWarnings("all")
    @PostMapping(value ="/create")
    public String createUser(@Validated @ModelAttribute("admCreateUser") User admCreatedUser, BindingResult bindingResult, String roleName) {
        userValidator.validate(admCreatedUser, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/user_create";
        }

        userService.addUser(admCreatedUser, roleName);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/update")
    public String updateForm(@ModelAttribute("id") Long id, Model model) {
        User userById = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("currentUser", userById);
        model.addAttribute("currentRole", userService.findRoleName(id));
        return "admin/user_update";
    }

    @PostMapping(value ="/update")
    public String updateUser(@Validated @ModelAttribute("currentUser") User updatedUser, BindingResult bindingResult, String roleName) {
        userValidator.validate(updatedUser, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/user_update";
        }

        userService.updateUser(updatedUser, roleName);
        return "redirect:/admin/users";
    }

    @PostMapping(value ="/delete")
    public String deleteUser(@ModelAttribute("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
