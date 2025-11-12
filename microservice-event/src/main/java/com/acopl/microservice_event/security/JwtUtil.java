package com.acopl.microservice_event.security;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret:ZmFrZXNlY3JldGtleWZha2U=}")
    private String secretBase64;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the userId (subject claim) from a JWT token
     * @param token the JWT token
     * @return the userId as Long, or null if token is invalid
     */
    public Long extractUserId(String token) {
        try {
            Claims claims = parseClaims(token);
            String subject = claims.getSubject();
            // The subject is the userId as a string
            return Long.parseLong(subject);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extracts the email from JWT token claims
     * @param token the JWT token
     * @return the email, or null if token is invalid
     */
    public String extractEmail(String token) {
        try {
            Claims claims = parseClaims(token);
            return (String) claims.get("email");
        } catch (Exception e) {
            return null;
        }
    }
}
