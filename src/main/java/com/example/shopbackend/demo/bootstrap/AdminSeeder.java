package com.example.shopbackend.demo.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.shopbackend.demo.user.Role;
import com.example.shopbackend.demo.user.User;
import com.example.shopbackend.demo.user.UserRepository;

@Component
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Remove & put in env
    // @Value("${ADMIN_EMAIL:}")
    private String adminEmail = "admin";

    // @Value("${ADMIN_PASSWORD:}")
    private String adminPassword = "123";

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            return;
        }

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = new User(
                adminEmail,
                passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);
        System.out.println("✅ Admin user created: " + adminEmail);
    }
}
