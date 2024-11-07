package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface AppService {
    List<User> getAllUsers();

    User getUserById(int id);

    void addOrUpdateUser(User user);

    void deleteUser(int id);

    User getUserByUsername(String username);

    Role getRoleByName(String roleName);

    Role getRoleById(int id);

    void addOrUpdateRole(Role role);

    List<Role> getAllRoles();
}
