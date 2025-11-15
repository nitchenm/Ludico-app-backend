package com.acopl.microservice_event.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acopl.microservice_event.security.JwtUtil;

import feign.RequestInterceptor;

@Configuration
public class FeignClientConfig {

    @Autowired
    private JwtUtil jwUtil;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");

            try {
                String systemToken = jwUtil.generateToken("system-service", 0L);
                requestTemplate.header("Authorization", "Bearer" + systemToken);
            } catch (Exception e) {

            }
        };
    }
}
