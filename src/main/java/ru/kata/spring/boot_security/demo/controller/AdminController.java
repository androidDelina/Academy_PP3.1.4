package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.AppService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.util.RandomPasswordUsernameGenerator;

import java.security.Principal;
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
    public String getAllUsers(Model model, Principal principal) {
        model.addAttribute("users", service.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("user", service.getUserByUsername(principal.getName()));

        return "/admin/users";
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

    @PostMapping("/{id}")
    public String updateUser (@ModelAttribute("user") User user, @PathVariable("id") int id, @RequestParam(name = "isAdminUpdate", required = false) String isAdminUpdate) {

        User existingUser = service.getUserById(id);
        System.out.println(existingUser.getId());

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setSex(user.getSex());
        existingUser.setCity(user.getCity());

        Set<Role> roles = existingUser.getRoles();
        if (roles.contains(service.getRoleByName("ROLE_ADMIN"))) {
            if (isAdminUpdate == null) {
                roles.remove(service.getRoleByName("ROLE_ADMIN"));
            }
        }

        if (!roles.contains(service.getRoleByName(isAdminUpdate))) {
            if (isAdminUpdate != null) {
                roles.add(service.getRoleByName(isAdminUpdate));
            }
        }
        existingUser.setRoles(roles);

        service.addOrUpdateUser(existingUser);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        service.deleteUser(id);
        return "redirect:/admin";
    }
}
