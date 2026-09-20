package com.example.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.auth.*;
import com.example.book.*;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner init(UserRepository users, BookRepository books, PasswordEncoder encoder, @Value("${app.admin.bootstrap-enabled:false}") boolean enabled, @Value("${app.admin.username:admin}") String admin, @Value("${app.admin.password:admin123}") String adminPassword) {
        return args -> {
            if (!users.existsByUsername("demo")) users.save(new User("demo", encoder.encode("password"), "USER"));
            if (enabled && !users.existsByUsername(admin))
                users.save(new User(admin, encoder.encode(adminPassword), "ADMIN"));
            if (books.count() == 0) {
                books.save(new Book("Effective Java", "Joshua Bloch", "9780134685991", new BigDecimal("650.00"), 10));
                books.save(new Book("Clean Code", "Robert C. Martin", "9780132350884", new BigDecimal("550.00"), 8));
            }
        };
    }
}
