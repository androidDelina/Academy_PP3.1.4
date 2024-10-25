package ru.kata.spring.boot_security.demo.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class RandomPasswordUsernameGenerator {
    public static String generatePassword(String username, int id) {
        String password = String.valueOf(username.hashCode() * id / 199 * 2 + (id +7));
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public static String generateUsername(String nameAndLastName, int id) {
        return nameAndLastName + id;
    }
}
