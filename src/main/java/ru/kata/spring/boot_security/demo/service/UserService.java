package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public void addUser(User user);
    public List<User> findAll();
    public Optional<User> findByUsername(String username);
    public Optional<User> findById(long id);
    public void updateUser(User user);
    public void deleteById(long id);
}
