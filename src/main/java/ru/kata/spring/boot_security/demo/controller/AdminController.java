package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.AppService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.util.RandomPasswordUsernameGenerator;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private AppService service;

    @Autowired
    public AdminController(AppService service) {
        this.service = service;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        model.addAttribute("users", service.getAllUsers());
        return "users";
    }

    @PostMapping()
    public String createUser(@ModelAttribute User user,
                             @RequestParam(name = "isAdmin", required = false) String isAdmin) {
        int userId = user.getId();
        user.setUsername(RandomPasswordUsernameGenerator.generateUsername(
                user.getName()+user.getSurname(),
                userId));
        user.setPassword(RandomPasswordUsernameGenerator.generatePassword(
                user.getUsername(),
                userId));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

//        Set<Role> roles = new HashSet<>();
//        roles.add(new Role(user, "ROLE_USER"));
//        if (roleString != null) {
//            roles.add(new Role(user, roleString));
//        }
//        user.setRoles(roles);

        Set<Role> roles = new HashSet<>();
        roles.add(service.getRoleByName("ROLE_USER"));
        if (isAdmin != null) {
            roles.add(service.getRoleByName(isAdmin));
        }
        user.setRoles(roles);

        service.addOrUpdateUser(user);
        System.out.println(user);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String addNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", service.getUserById(id));
        return "user";
    }

    @PostMapping("/user/edit")
    public String updateUser(@ModelAttribute User user) {

        User existingUser = service.getUserById(user.getId());

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setSex(user.getSex());
        existingUser.setCity(user.getCity());

        System.out.println(user);
        service.addOrUpdateUser(existingUser);
        return "redirect:/admin";
    }

    @PostMapping("/user")
    public String deleteUser(@RequestParam("id") int id) {
        service.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/user/edit")
    public String getEditForm(@RequestParam("id") int id, Model model) {
        model.addAttribute("user", service.getUserById(id));
        return "editUser";
    }
}
