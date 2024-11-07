package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AppService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
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
        model.addAttribute("roles", service.getAllRoles());

        return "/admin/users";
    }

    @PostMapping()
    public String createUser(@ModelAttribute User user,
                             @RequestParam(name = "rolesNew") List<Integer> rolesID) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        Set<Role> roles = new HashSet<>();
        for (int roleId : rolesID) {
            roles.add(service.getRoleById(roleId));
        }
        user.setRoles(roles);


        service.addOrUpdateUser(user);
        System.out.println(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") int id, @RequestParam(name = "rolesUpdate", required = false) List<Integer> rolesId) {

        User existingUser = service.getUserById(id);
        System.out.println(existingUser.getId());

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setSex(user.getSex());
        existingUser.setCity(user.getCity());

        Set<Role> roles = existingUser.getRoles();
        if (rolesId != null) {
            roles = new HashSet<>();
            for (int roleId : rolesId) {
                roles.add(service.getRoleById(roleId));
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
