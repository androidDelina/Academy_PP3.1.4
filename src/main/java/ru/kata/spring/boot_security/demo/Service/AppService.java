package ru.kata.spring.boot_security.demo.Service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface AppService {
    public List<User> getAllUsers();
    public User getUserById(int id);
    public void addOrUpdateUser(User user);
    public void deleteUser(int id);
    public User getUserByUsername(String username);
}
