package com.acopl.microservice_support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/suppport").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/suppport").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/suppport").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/suppport").permitAll()
                        .requestMatchers("/api/v1/support/**").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}
