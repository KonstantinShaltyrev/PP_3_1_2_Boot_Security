package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public void save(Role role);
    public List<Role> findAll();

    public Optional<Role> findByRoleName(String roleName);

    public Optional<Role> findRoleById(long id);
    public void delete(Role role);
    public void deleteRoleById(long id);
    public void checkRoles();
}
