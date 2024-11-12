package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AppService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class RestUserController {
    private AppService appService;

    @Autowired
    public RestUserController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> apiGetAllUsers() {
        return new ResponseEntity<>(appService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> apiGetUserInfo(@PathVariable("id") int id) {
        User user = appService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> apiCreateUser(@RequestBody User user) {
        appService.createUser(user);
        URI location = URI.create("/api/" + user.getId());
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> apiUpdateUser(@RequestBody User user, @PathVariable(name = "id") int id) {
        appService.updateUser(user, id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> apiDeleteUser(@PathVariable("id") int id) {
        appService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> apiGetRoles() {
        return new ResponseEntity<>(appService.getAllRoles(), HttpStatus.OK);
    }
}

