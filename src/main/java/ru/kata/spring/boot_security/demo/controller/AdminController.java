package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AppService;

import java.security.Principal;

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
        model.addAttribute("username", principal.getName());
        model.addAttribute("roles", service.getAllRoles());

        return "/admin/users";
    }
}
