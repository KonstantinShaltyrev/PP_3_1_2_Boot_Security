package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void addUser(User user, String roleName) {
        Set<Role> rolesSet = new HashSet<>();

        roleService.checkRoles();
        if (roleName.equals("ROLE_ADMIN")) {
            rolesSet.add(roleService.findByRoleName("ROLE_ADMIN").get());
        }
        rolesSet.add(roleService.findByRoleName("ROLE_USER").get());
        user.setRoles(rolesSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user, String roleName) {
        Set<Role> rolesSet = new HashSet<>();

        if (roleName.equals("ROLE_ADMIN")) {
            rolesSet.add(roleService.findByRoleName("ROLE_ADMIN").get());
        }
        rolesSet.add(roleService.findByRoleName("ROLE_USER").get());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(rolesSet);
        userRepository.save(user);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }


    @Override
    @Transactional
    public String findRoleName (Long id) {
        User userById = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<String> currentRoles = new ArrayList<>();

        userById.getRoles().forEach(p -> currentRoles.add(p.getRoleName()));
        return currentRoles.contains("ROLE_ADMIN") ? "ROLE_ADMIN" : "ROLE_USER";
    }
}
