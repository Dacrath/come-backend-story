package com.example.come_backend_story.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()   // Allow H2 console
            .anyRequest().permitAll()                        // Allow all other endpoints for now
        )
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**")       // Disable CSRF for H2
        )
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.disable())   // Critical for H2
        );

    return http.build();
  }
}