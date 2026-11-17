// SecurityConfig will hold the rules that control access to your Admin and Librarian APIs.

package com.library.library_management_system.config;

import java.security.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
// Bean: Spring to use this security configuration.

    
    @Bean
    // AuthenticationManager is responsible for processing authentication requests in Spring Security.
    // connecting our login system to Spring Security properly
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    // HttpSecurity lets us configure security rules.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        // csrf.disable() temporarily disables CSRF protection so our initial JavaScript login requests can work more simply.
        http.csrf(csrf -> csrf.disable().authorizeHttpRequests(auth -> auth

            //    /auth/** – Login and logout endpoints are accessible without an existing login.
            //   /librarians/** – Only Admins can access librarian management endpoints.
                            .requestMatchers("/auth/**").permitAll().requestMatchers("/librarians/**").hasRole("ADMIN")

                            // .anyRequest().authenticated() – Nobody can use protected parts of the application without logging in.
                            .anyRequest().authenticated()));

        // http.build() creates the security filter chain.
        return http.build();
    }
}
