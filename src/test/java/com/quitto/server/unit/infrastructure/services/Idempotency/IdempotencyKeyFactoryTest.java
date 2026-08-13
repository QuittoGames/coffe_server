package com.quitto.server.unit.infrastructure.services.Idempotency;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.interfaces.OperationKey.OperationKeyFactory;
import com.quitto.server.domain.valueobject.IdempotencyKey.IdempotencyKey;
import com.quitto.server.infrastructure.services.Idempotency.IdempotencyKeyFactory;

class IdempotencyKeyFactoryTest {

    @Mock
    private IdempotencyKeyFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWithDefaultTTL() {
        // When
        OperationKey key = factory.create();
        
        // Then
        assertNotNull(key);
        assertNotNull(key.getId());
        assertNotNull(key.getValue());
        assertNotNull(key.getCreationDate());
        assertNotNull(key.expiresAt());
        
        // The TTL should be approximately 10 minutes (default)
        Duration actualTTL = Duration.between(key.getCreationDate(), key.expiresAt().atZone(ZoneOffset.UTC).toLocalDateTime());
        assertTrue(actualTTL.compareTo(Duration.ofMinutes(10)) >= 0 && actualTTL.compareTo(Duration.ofMinutes(10)) <= 1);
    }

    @Test
    void testCreateWithCustomTTL() {
        // Given
        Duration customTTL = Duration.ofMinutes(30);
        
        // When
        OperationKey key = factory.create(customTTL);
        
        // Then
        assertNotNull(key);
        assertNotNull(key.getId());
        assertNotNull(key.getValue());
        assertNotNull(key.getCreationDate());
        assertNotNull(key.expiresAt());
        
        // The TTL should be approximately 30 minutes
        Duration actualTTL = Duration.between(key.getCreationDate(), key.expiresAt().atZone(ZoneOffset.UTC).toLocalDateTime());
        assertTrue(actualTTL.compareTo(Duration.ofMinutes(30)) >= 0 && actualTTL.compareTo(Duration.ofMinutes(30)) <= 1);
    }

    @Test
    void testCreateWithNullTTLUsesDefault() {
        // When
        OperationKey key = factory.create(null);
        
        // Then
        assertNotNull(key);
        assertNotNull(key.getId());
        assertNotNull(key.getValue());
        assertNotNull(key.getCreationDate());
        assertNotNull(key.expiresAt());
        
        // The TTL should be approximately 10 minutes (default)
        Duration actualTTL = Duration.between(key.getCreationDate(), key.expiresAt().atZone(ZoneOffset.UTC).toLocalDateTime());
        assertTrue(actualTTL.compareTo(Duration.ofMinutes(10)) >= 0 && actualTTL.compareTo(Duration.ofMinutes(10)) <= 1);
    }

    @Test
    void testCreateGeneratesDifferentIds() {
        // When
        OperationKey key1 = factory.create();
        OperationKey key2 = factory.create();
        
        // Then
        assertNotEquals(key1.getId(), key2.getId());
        assertNotEquals(key1.getValue(), key2.getValue());
    }
}
