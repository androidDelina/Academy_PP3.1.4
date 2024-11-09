package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface AppService {
    List<User> getAllUsers();

    User getUserById(int id);

    void createUser(User user, List<Integer> rolesID);

    void updateUser(User user, int id, List<Integer> rolesId, String cit);

    void deleteUser(int id);

    User getUserByUsername(String username);

    Role getRoleByName(String roleName);

    Role getRoleById(int id);

    void addOrUpdateRole(Role role);

    List<Role> getAllRoles();

    Set<Role> getRolesByIds(List<Integer> ids, Set<Role> existingRoles);
}
