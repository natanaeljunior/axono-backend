package com.axono.config;

import com.axono.domain.User;
import com.axono.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setName("Administrador Axono");
                admin.setEmail("admin@axono.com");
                admin.setPasswordHash(passwordEncoder.encode("admin@123"));
                admin.setRoles(Arrays.asList(User.UserRole.values()));
                admin.setMatricula("ADM001");
                admin.setFirstAccessPending(false);
                userRepository.save(admin);
            }
        };
    }
}
