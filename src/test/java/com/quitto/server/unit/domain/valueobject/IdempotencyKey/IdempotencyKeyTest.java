package com.quitto.server.unit.domain.valueobject.IdempotencyKey;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.quitto.server.domain.valueobject.IdempotencyKey.IdempotencyKey;

class IdempotencyKeyTest {

    private IdempotencyKey key;
    private UUID testId;
    private UUID testValue;
    private LocalDateTime testCreationDate;
    private LocalDateTime testExpirationDate;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testValue = UUID.randomUUID();
        testCreationDate = LocalDateTime.now(ZoneOffset.UTC);
        testExpirationDate = testCreationDate.plus(Duration.ofMinutes(10));
        
        key = new IdempotencyKey(testId, testValue, testCreationDate, testExpirationDate);
    }

    @Test
    void testGetId() {
        assertEquals(testId, key.getId());
    }

    @Test
    void testGetValue() {
        assertEquals(testValue, key.getValue());
    }

    @Test
    void testGetCreationDate() {
        assertEquals(testCreationDate, key.getCreationDate());
    }

    @Test
    void testCreatedAt() {
        assertEquals(testCreationDate.toInstant(ZoneOffset.UTC), key.createdAt());
    }

    @Test
    void testExpiresAt() {
        assertEquals(testExpirationDate.toInstant(ZoneOffset.UTC), key.expiresAt());
    }

    @Test
    void testIsNotExpiredInitially() {
        assertFalse(key.isExpired());
    }

    @Test
    void testIsNotValidUntilValidated() {
        assertFalse(key.isValid());
    }

    @Test
    void testValidateMakesKeyValid() {
        key.validate();
        assertTrue(key.isValid());
    }

    @Test
    void testIsExpiredAfterExpirationTime() {
        // Set expiration date to 5 minutes ago
        LocalDateTime expiredDate = LocalDateTime.now(ZoneOffset.UTC).minus(Duration.ofMinutes(5));
        IdempotencyKey expiredKey = new IdempotencyKey(UUID.randomUUID(), UUID.randomUUID(), 
                                                   LocalDateTime.now(ZoneOffset.UTC).minus(Duration.ofMinutes(15)), 
                                                   expiredDate);
        
        assertTrue(expiredKey.isExpired());
    }

    @Test
    void testValidatedKeyReturnsFalseWhenNotValidated() {
        assertFalse(key.validatedKey());
    }

    @Test
    void testValidatedKeyReturnsTrueWhenValidated() {
        key.validate();
        assertTrue(key.validatedKey());
    }
}
