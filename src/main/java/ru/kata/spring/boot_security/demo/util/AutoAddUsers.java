package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import ru.kata.spring.boot_security.demo.service.AppService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
public class AutoAddUsers {

    private AppService appService;

    @Autowired
    public AutoAddUsers(AppService appService) {
        this.appService = appService;
    }

    @PostConstruct
    public void autoAddUser() {
        Role roleUser = new Role("ROLE_USER");
        appService.addOrUpdateRole(roleUser);
        Role roleAdmin = new Role("ROLE_ADMIN");
        appService.addOrUpdateRole(roleAdmin);

        Role userRole = appService.getRoleByName("ROLE_USER");
        Role adminRole = appService.getRoleByName("ROLE_ADMIN");


        User user = new User("user",
                BCrypt.hashpw("user", BCrypt.gensalt()),
                new HashSet<>(Arrays.asList(userRole)),
                true,
                true,
                true,
                true,
                "user",
                "user",
                "female",
                "Moscow");

        User admin = new User("admin",
                BCrypt.hashpw("admin", BCrypt.gensalt()),
                new HashSet<>(Arrays.asList(userRole, adminRole)),
                true,
                true,
                true,
                true,
                "admin",
                "admin",
                "male",
                "Moscow");

        appService.addOrUpdateUser(user);
        appService.addOrUpdateUser(admin);
    }
}
