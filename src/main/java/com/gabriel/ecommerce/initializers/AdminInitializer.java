package com.gabriel.ecommerce.initializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.gabriel.ecommerce.entity.User;
import com.gabriel.ecommerce.enums.Role;
import com.gabriel.ecommerce.repository.UserRepository;


@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Override
    public void run(String... args) {
        if (repository.findByRole(Role.ADMIN).isEmpty()) {
            String encryptedPassword = new BCryptPasswordEncoder().encode("password");
            User admin = new User("admin", encryptedPassword, Role.ADMIN, "example@email.com");
            repository.save(admin);
            System.out.println("Admin user created successfully!");
        }
    }
}