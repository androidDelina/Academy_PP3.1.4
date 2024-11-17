package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${server.url}")
    private String serverUrl;

    @Value("${server.port}")
    private String serverPort;

    public String getServerUrl() {
        return serverUrl;
    }

    public String getServerPort() {
        return serverPort;
    }
}
