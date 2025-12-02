package com.acopl.microservice_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(
                    "/auth/**",
                    "/api/v1/users/**",
                    "/api/v1/support/**",
                    "/api/v1/events/**",
                    "/v3/api-docs/**",
                    "/actuator/**"
                ).permitAll()
                .anyExchange().permitAll()
            )
            .build();
    }
}