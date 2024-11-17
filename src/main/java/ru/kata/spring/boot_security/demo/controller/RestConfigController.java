package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.configs.AppConfig;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestConfigController {

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("serverUrl", appConfig.getServerUrl());
        config.put("serverPort", appConfig.getServerPort());
        return config;
    }

}
