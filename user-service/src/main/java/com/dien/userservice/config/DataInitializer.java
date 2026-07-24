package com.dien.userservice.config;

import com.dien.userservice.entity.User;
import com.dien.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(UserRepository repository, PasswordEncoder encoder) {
        return args -> {
            createUserIfMissing(repository, encoder, "userA", "123456", "USER");
            createUserIfMissing(repository, encoder, "userB", "123456", "USER");
            createUserIfMissing(repository, encoder, "admin", "admin123", "ADMIN");
        };
    }

    private void createUserIfMissing(
            UserRepository repository,
            PasswordEncoder encoder,
            String username,
            String password,
            String role) {
        if (repository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            user.setRole(role);
            repository.save(user);
        }
    }
}
