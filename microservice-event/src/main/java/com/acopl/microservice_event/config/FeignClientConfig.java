package com.acopl.microservice_event.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.acopl.microservice_event.security.JwtUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignClientConfig {

    @Autowired
    private JwtUtil jwUtil;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Obtener los atributos de la solicitud actual
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes();
                if (attributes != null) {
                    // Obtener el encabezado "Authorization" de la solicitud entrante (de Postman)
                    String authorizationHeader = attributes.getRequest().getHeader("Authorization");
                    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                        // Añadir ese encabezado a la solicitud saliente de Feign
                        template.header("Authorization", authorizationHeader);
                    }
                }
            }
        };
    }

}
