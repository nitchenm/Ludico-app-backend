package com.acopl.microservice_user;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.acopl.microservice_user.security.JwtUtil;

import io.jsonwebtoken.Claims;

/**
 * Test suite for JwtUtil in microservice-user
 * Tests JWT token generation, validation, and claim extraction
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secretBase64;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        secretBase64 = "aW1wb3J0YW50X3NlY3JldF9jaGFuZ2VfdGhpc19pbl9wcm9kdWN0aW9uX3RvX2tSMjU2X2tleQ==";
        ReflectionTestUtils.setField(jwtUtil, "secretBase64", secretBase64);
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);
    }

    /**
     * Test token generation
     */
    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(1L, "test@example.com");

        assertNotNull(token);
        assertTrue(token.contains("."), "Token should contain dots (JWT format)");
    }

    /**
     * Test token contains correct email as subject
     */
    @Test
    void testGenerateToken_containsCorrectSubject() {
        String token = jwtUtil.generateToken(1L, "test@example.com");
        Claims claims = jwtUtil.parseClaims(token);

        assertEquals("test@example.com", claims.getSubject(), "Subject should be email");
    }

    /**
     * Test token contains userId claim
     */
    @Test
    void testGenerateToken_containsUserIdClaim() {
        String token = jwtUtil.generateToken(42L, "test@example.com");
        Claims claims = jwtUtil.parseClaims(token);

        assertEquals(42, claims.get("userId"), "userId claim should match");
    }

    /**
     * Test token has expiration
     */
    @Test
    void testGenerateToken_hasExpiration() {
        String token = jwtUtil.generateToken(1L, "test@example.com");
        Claims claims = jwtUtil.parseClaims(token);

        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()), "Token should not be expired");
    }

    /**
     * Test valid token parsing
     */
    @Test
    void testParseClaims_validToken() {
        String token = jwtUtil.generateToken(1L, "test@example.com");
        Claims claims = jwtUtil.parseClaims(token);

        assertNotNull(claims);
        assertNotNull(claims.getSubject());
        assertNotNull(claims.getIssuedAt());
    }

    /**
     * Test invalid token parsing
     */
    @Test
    void testParseClaims_invalidToken() {
        String invalidToken = "invalid.token.here";

        assertThrows(Exception.class, () -> jwtUtil.parseClaims(invalidToken));
    }

    /**
     * Test malformed token
     */
    @Test
    void testParseClaims_malformedToken() {
        String malformedToken = "notavalidtoken";

        assertThrows(Exception.class, () -> jwtUtil.parseClaims(malformedToken));
    }

    /**
     * Test token can be generated and parsed multiple times
     */
    @Test
    void testTokenGenerationAndParsing_multiple() {
        for (int i = 0; i < 5; i++) {
            Long userId = (long) i;
            String email = "user" + i + "@example.com";
            
            String token = jwtUtil.generateToken(userId, email);
            Claims claims = jwtUtil.parseClaims(token);

            assertEquals(email, claims.getSubject());
            assertEquals(i, ((Number) claims.get("userId")).intValue());
        }
    }

    /**
     * Test JWT token format (3 parts separated by dots)
     */
    @Test
    void testTokenFormat() {
        String token = jwtUtil.generateToken(1L, "test@example.com");
        String[] parts = token.split("\\.");

        assertEquals(3, parts.length, "JWT should have 3 parts");
    }

    /**
     * Test token expiration time is set
     */
    @Test
    void testTokenExpiration_isSet() {
        long beforeGeneration = System.currentTimeMillis();
        String token = jwtUtil.generateToken(1L, "test@example.com");
        long afterGeneration = System.currentTimeMillis();

        Claims claims = jwtUtil.parseClaims(token);
        Date expiration = claims.getExpiration();

        assertTrue(expiration.getTime() > afterGeneration, "Expiration should be in the future");
        assertTrue(expiration.getTime() > beforeGeneration, "Expiration should be after token generation");
    }

    /**
     * Test different userId values
     */
    @Test
    void testDifferentUserIds() {
        String token1 = jwtUtil.generateToken(1L, "user1@example.com");
        String token2 = jwtUtil.generateToken(999L, "user999@example.com");

        Claims claims1 = jwtUtil.parseClaims(token1);
        Claims claims2 = jwtUtil.parseClaims(token2);

        assertEquals(1, ((Number) claims1.get("userId")).intValue());
        assertEquals(999, ((Number) claims2.get("userId")).intValue());
    }

    /**
     * Test token with special characters in email
     */
    @Test
    void testTokenWithSpecialCharactersInEmail() {
        String token = jwtUtil.generateToken(1L, "user+test@sub.example.com");
        Claims claims = jwtUtil.parseClaims(token);

        assertEquals("user+test@sub.example.com", claims.getSubject());
    }

    /**
     * Test issued at timestamp is set
     */
    @Test
    void testTokenIssuedAt_isSet() {
        String token = jwtUtil.generateToken(1L, "test@example.com");
        Claims claims = jwtUtil.parseClaims(token);

        assertNotNull(claims.getIssuedAt());
        assertTrue(claims.getIssuedAt().before(new Date()), "Issued at should be before now");
    }
}
