package com.acopl.microservice_user.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.acopl.microservice_user.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Test configuration for Spring Security
 * Provides beans required for testing without full security context
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil() {
        // Provide a simple JwtUtil bean for tests with a fixed key
        return new JwtUtil() {
            private final String secretBase64 = "ZmFrZXNlY3JldGtleWZha2U="; // base64 'fakesecretkeyfake'
            private final long expirationMs = 3600000L;

            private Key getSigningKey() {
                byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
                return Keys.hmacShaKeyFor(keyBytes);
            }

            @Override
            public String generateToken(Long userId, String email) {
                Date now = new Date();
                Date exp = new Date(now.getTime() + expirationMs);
                return Jwts.builder()
                        .setSubject(email)
                        .addClaims(Map.of("userId", userId))
                        .setIssuedAt(now)
                        .setExpiration(exp)
                        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                        .compact();
            }

            @Override
            public Claims parseClaims(String token) {
                return Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            }
        };
    }
}
