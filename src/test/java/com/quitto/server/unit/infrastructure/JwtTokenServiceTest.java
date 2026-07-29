package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.quitto.server.infrastructure.services.Auth.Token.Jtw.JwtTokenService;


import com.auth0.jwt.exceptions.JWTDecodeException;

class JwtTokenServiceTest {

    private JwtTokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new JwtTokenService();
        ReflectionTestUtils.setField(tokenService, "KEY", "test-secret-key-for-jwt-unit-tests");
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = tokenService.generateToken(42L);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void generateAndVerifyToken_succeeds() {
        String token = tokenService.generateToken(99L);
        assertTrue(tokenService.verifyToken(token));
    }

    @Test
    void verifyToken_returnsFalseOnInvalidToken() {
        assertFalse(tokenService.verifyToken("invalid-token-string"));
    }

    @Test
    void verifyToken_returnsFalseOnEmptyToken() {
        assertFalse(tokenService.verifyToken(""));
    }

    @Test
    void generateAndExtractSubject_matches() {
        String token = tokenService.generateToken(123L);
        var extracted = tokenService.extractIdSubject(token);
        assertTrue(extracted.isPresent());
        assertEquals(123L, extracted.get());
    }

    @Test
    void extractSubject_throwsOnInvalidToken() {
        assertThrows(JWTDecodeException.class,
            () -> tokenService.extractIdSubject("bad-token"));
    }

    @Test
    void generatedTokenHasThreeParts() {
        String token = tokenService.generateToken(1L);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT must have 3 dot-separated parts");
    }

    @Test
    void generateToken_differentIdsProduceDifferentTokens() {
        String token1 = tokenService.generateToken(1L);
        String token2 = tokenService.generateToken(2L);
        assertNotEquals(token1, token2);
    }

    @Test
    void verifyToken_returnsFalseOnTokenFromDifferentKey() {
        JwtTokenService otherService = new JwtTokenService();
        ReflectionTestUtils.setField(otherService, "KEY", "different-secret-key");

        String token = otherService.generateToken(1L);
        assertFalse(tokenService.verifyToken(token));
    }
}
