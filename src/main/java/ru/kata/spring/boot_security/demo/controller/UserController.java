package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.Service.AppService;
import ru.kata.spring.boot_security.demo.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

    private AppService appService;

    @Autowired
    public UserController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping()
    public String getUserInfo(Model model) {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }
}
