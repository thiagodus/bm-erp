package com.bm.erp.user;

import com.bm.erp.user.entity.User;
import com.bm.erp.user.entity.UserRole;
import com.bm.erp.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@erp.com").isEmpty()) {
                User user = new User();
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setEmail("admin@erp.com");
                user.setRole(UserRole.ADMIN);

                userRepository.save(user);
            }
            if (userRepository.findByEmail("user@erp.com").isEmpty()) {
                User user = new User();
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setEmail("user@erp.com");
                user.setRole(UserRole.USER);

                userRepository.save(user);
            }
        };
    }

}
