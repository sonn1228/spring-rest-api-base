package com.sonnguyen.base.config;

import com.sonnguyen.base.model.Role;
import com.sonnguyen.base.model.User;
import com.sonnguyen.base.repository.RoleRepository;
import com.sonnguyen.base.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                Role userRole = new Role();
                userRole.setName("USER");
                userRole.setDescription("Role for regular users");
                roleRepository.save(userRole);
            }

            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                adminRole.setDescription("Role for administrators");
                roleRepository.save(adminRole);
            }

            if (userRepository.findByUsername("admin") == null) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin"));
                adminUser.setRoles(Set.of(roleRepository.findByName("ADMIN").get()));
                userRepository.save(adminUser);
            }
        };
    }
}
