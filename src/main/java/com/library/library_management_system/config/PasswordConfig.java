package com.library.library_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration tells Spring this class contains configuration.
// @Bean makes the password encoder available throughout your application.
// BCryptPasswordEncoder securely hashes passwords.
// PasswordEncoder is the common interface we'll use in our service.

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
