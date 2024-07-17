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
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserValidator userValidator;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserValidator userValidator, UserService userService, RoleService roleService) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.roleService = roleService;
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
    public String createUser(@Validated @ModelAttribute("admCreateUser") User admCreateUser, BindingResult bindingResult, String roleName) {
        userValidator.validate(admCreateUser, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/user_create";
        }
        if (roleService.findByRoleName("ROLE_ADMIN").isEmpty()) {
            Role role = new Role();

            role.setRoleName("ROLE_ADMIN");
            roleService.save(role);
            if (roleService.findByRoleName("ROLE_USER").isEmpty()) {
                role.setRoleName("ROLE_User");
                roleService.save(role);
            }

        }

        Set<Role> rolesSet = new HashSet<>();

        if (roleName.equals("ROLE_ADMIN")) {
            rolesSet.add(roleService.findByRoleName("ROLE_ADMIN").get());
        }
        rolesSet.add(roleService.findByRoleName("ROLE_USER").get());
        admCreateUser.setRoles(rolesSet);
        userService.addUser(admCreateUser);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/update")
    public String updateForm(@ModelAttribute("id") Long id, Model model) {
        User userById = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<String> currentRoles = new ArrayList<>();

        userById.getRoles().forEach(p -> currentRoles.add(p.getRoleName()));
        String currentRole = currentRoles.contains("ROLE_ADMIN") ? "ROLE_ADMIN" : "ROLE_USER";

        model.addAttribute("currentUser", userById);
        model.addAttribute("currentRole", currentRole);
        return "admin/user_update";
    }

    @PostMapping(value ="/update")
    public String updateUser(@Validated @ModelAttribute("currentUser") User updatedUser, BindingResult bindingResult, String roleName) {
        userValidator.validate(updatedUser, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admin/user_update";
        }

        Set<Role> rolesSet = new HashSet<>();

        if (roleName.equals("ROLE_ADMIN")) {
            rolesSet.add(roleService.findByRoleName("ROLE_ADMIN").get());
        }
        rolesSet.add(roleService.findByRoleName("ROLE_USER").get());
        updatedUser.setRoles(rolesSet);
        userService.updateUser(updatedUser);
        return "redirect:/admin/users";
    }

    @PostMapping(value ="/delete")
    public String deleteUser(@ModelAttribute("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
