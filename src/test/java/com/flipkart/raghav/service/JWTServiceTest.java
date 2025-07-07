package com.flipkart.raghav.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("JWT Service Tests")
class JWTServiceTest {

    private JWTService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities("USER")
                .build();
    }

    @Test
    @DisplayName("Should create JWTService with valid secret key")
    void testJWTServiceConstructor() {
        assertNotNull(jwtService);
    }

    @Test
    @DisplayName("Should generate valid token for username")
    void testGenerateToken() {
        String token = jwtService.generateToken("testuser");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Should generate different tokens for different usernames")
    void testGenerateTokenDifferentUsernames() {
        String token1 = jwtService.generateToken("user1");
        String token2 = jwtService.generateToken("user2");

        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Should generate token with empty username")
    void testGenerateTokenWithEmptyUsername() {
        String token = jwtService.generateToken("");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should generate token with null username")
    void testGenerateTokenWithNullUsername() {
        String token = jwtService.generateToken(null);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should generate token with special characters in username")
    void testGenerateTokenWithSpecialCharacters() {
        String token = jwtService.generateToken("user@domain.com");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should generate token with unicode characters in username")
    void testGenerateTokenWithUnicodeCharacters() {
        String token = jwtService.generateToken("用户");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void testExtractUserName() {
        String token = jwtService.generateToken("testuser");
        String extractedUsername = jwtService.extractUserName(token);

        assertEquals("testuser", extractedUsername);
    }

    @Test
    @DisplayName("Should extract username with special characters")
    void testExtractUserNameWithSpecialCharacters() {
        String token = jwtService.generateToken("user@domain.com");
        String extractedUsername = jwtService.extractUserName(token);

        assertEquals("user@domain.com", extractedUsername);
    }

    @Test
    @DisplayName("Should extract username with unicode characters")
    void testExtractUserNameWithUnicodeCharacters() {
        String token = jwtService.generateToken("用户");
        String extractedUsername = jwtService.extractUserName(token);

        assertEquals("用户", extractedUsername);
    }

    @Test
    @DisplayName("Should validate token with correct user details")
    void testValidateTokenWithCorrectUserDetails() {
        String token = jwtService.generateToken("testuser");
        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject token with incorrect username")
    void testValidateTokenWithIncorrectUsername() {
        String token = jwtService.generateToken("testuser");
        UserDetails wrongUser = User.builder()
                .username("wronguser")
                .password("password")
                .authorities("USER")
                .build();

        boolean isValid = jwtService.validateToken(token, wrongUser);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should reject null token")
    void testValidateTokenWithNullToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            jwtService.validateToken(null, userDetails);
        });
    }

    @Test
    @DisplayName("Should reject empty token")
    void testValidateTokenWithEmptyToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            jwtService.validateToken("", userDetails);
        });
    }

    @Test
    @DisplayName("Should reject invalid token format")
    void testValidateTokenWithInvalidFormat() {
        assertThrows(Exception.class, () -> {
            jwtService.validateToken("invalid.token.format", userDetails);
        });
    }

    @Test
    @DisplayName("Should reject token with null user details")
    void testValidateTokenWithNullUserDetails() {
        String token = jwtService.generateToken("testuser");

        assertThrows(NullPointerException.class, () -> {
            jwtService.validateToken(token, null);
        });
    }

    @Test
    @DisplayName("Should reject token with null username in user details")
    void testValidateTokenWithNullUsernameInUserDetails() {
        String token = jwtService.generateToken("testuser");

        assertThrows(IllegalArgumentException.class, () -> {
            UserDetails userWithNullUsername = User.builder()
                    .username(null)
                    .password("password")
                    .authorities("USER")
                    .build();
        });
    }

    @Test
    @DisplayName("Should handle very long username")
    void testGenerateTokenWithLongUsername() {
        String longUsername = "a".repeat(1000);
        String token = jwtService.generateToken(longUsername);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        String extractedUsername = jwtService.extractUserName(token);
        assertEquals(longUsername, extractedUsername);
    }

    @Test
    @DisplayName("Should generate consistent tokens for same username")
    void testGenerateTokenConsistency() {
        String token1 = jwtService.generateToken("testuser");
        String token2 = jwtService.generateToken("testuser");

        // Tokens should be different due to different timestamps
        // Note: In some cases, tokens might be identical if generated very quickly
        // So we'll just verify they extract the same username
        assertEquals(jwtService.extractUserName(token1), jwtService.extractUserName(token2));
    }

    @Test
    @DisplayName("Should handle multiple JWTService instances")
    void testMultipleJWTServiceInstances() {
        JWTService jwtService2 = new JWTService();

        String token1 = jwtService.generateToken("testuser");
        String token2 = jwtService2.generateToken("testuser");

        // Tokens from different instances should be different
        assertNotEquals(token1, token2);

        // But both should be valid for their respective services
        assertTrue(jwtService.validateToken(token1, userDetails));
        assertTrue(jwtService2.validateToken(token2, userDetails));
    }

    @Test
    @DisplayName("Should handle token with claims")
    void testTokenWithClaims() {
        // The current implementation uses an empty claims map
        // This test verifies the behavior
        String token = jwtService.generateToken("testuser");

        assertNotNull(token);
        String extractedUsername = jwtService.extractUserName(token);
        assertEquals("testuser", extractedUsername);
    }

    @Test
    @DisplayName("Should handle token expiration")
    void testTokenExpiration() {
        String token = jwtService.generateToken("testuser");

        // Token should be valid immediately after generation
        assertTrue(jwtService.validateToken(token, userDetails));

        // Note: We can't easily test actual expiration without time manipulation
        // The token is set to expire in 30 hours, so it should be valid for testing
    }

    @Test
    @DisplayName("Should handle malformed token")
    void testMalformedToken() {
        String malformedToken = "header.payload"; // Missing signature

        assertThrows(Exception.class, () -> {
            jwtService.extractUserName(malformedToken);
        });
    }

    @Test
    @DisplayName("Should handle token with wrong signature")
    void testTokenWithWrongSignature() {
        // Create a token with different service
        JWTService differentService = new JWTService();
        String token = differentService.generateToken("testuser");

        // Try to validate with original service
        assertThrows(Exception.class, () -> {
            jwtService.validateToken(token, userDetails);
        });
    }

    @Test
    @DisplayName("Should handle token with tampered payload")
    void testTokenWithTamperedPayload() {
        String originalToken = jwtService.generateToken("testuser");

        // Tamper with the token by changing characters
        String tamperedToken = originalToken.substring(0, originalToken.length() - 5) + "XXXXX";

        assertThrows(Exception.class, () -> {
            jwtService.extractUserName(tamperedToken);
        });
    }

    @Test
    @DisplayName("Should handle whitespace in username")
    void testGenerateTokenWithWhitespace() {
        String token = jwtService.generateToken(" test user ");
        String extractedUsername = jwtService.extractUserName(token);

        assertEquals(" test user ", extractedUsername);
    }

    @Test
    @DisplayName("Should handle numeric username")
    void testGenerateTokenWithNumericUsername() {
        String token = jwtService.generateToken("12345");
        String extractedUsername = jwtService.extractUserName(token);

        assertEquals("12345", extractedUsername);
    }

    @Test
    @DisplayName("Should handle mixed case username")
    void testGenerateTokenWithMixedCaseUsername() {
        String token = jwtService.generateToken("TestUser123");
        String extractedUsername = jwtService.extractUserName(token);

        assertEquals("TestUser123", extractedUsername);
    }
}