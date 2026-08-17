package com.quitto.server.unit.infrastructure.services.Idempotency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.quitto.server.domain.exception.OperationKey.OperationKeyIdNotFoundException;
import com.quitto.server.domain.exception.OperationKey.OperationKeyNotFoundException;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.interfaces.OperationKey.OperationKeyFactory;
import com.quitto.server.infrastructure.services.Cache.CacheService;
import com.quitto.server.infrastructure.services.Idempotency.IdempotencyManager;

class IdempotencyManagerTest {

    @Mock
    private CacheService cacheService;

    @Mock
    private OperationKeyFactory factory;

    private IdempotencyManager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new IdempotencyManager(cacheService, factory);
    }

    @Test
    void validatedReturnsTrueWhenCacheHit() {
        OperationKey key = mock(OperationKey.class);
        UUID id = UUID.randomUUID();
        when(key.getId()).thenReturn(id);
        when(key.getValue()).thenReturn(UUID.randomUUID());
        when(cacheService.search(id.toString())).thenReturn(Optional.of("stored-value"));

        boolean result = manager.validated(key);

        assertTrue(result);
        verify(cacheService).search(id.toString());
    }

    @Test
    void validatedReturnsFalseWhenCacheMiss() {
        OperationKey key = mock(OperationKey.class);
        UUID id = UUID.randomUUID();
        when(key.getId()).thenReturn(id);
        when(key.getValue()).thenReturn(UUID.randomUUID());
        when(cacheService.search(id.toString())).thenReturn(Optional.empty());

        boolean result = manager.validated(key);

        assertFalse(result);
        verify(cacheService).search(id.toString());
    }

    @Test
    void validatedThrowsWhenIdIsNull() {
        OperationKey key = mock(OperationKey.class);
        when(key.getId()).thenReturn(null);
        when(key.getValue()).thenReturn(UUID.randomUUID());

        assertThrows(OperationKeyIdNotFoundException.class, () -> manager.validated(key));

        verify(cacheService, never()).search(anyString());
    }

    @Test
    void validatedThrowsWhenValueIsNull() {
        OperationKey key = mock(OperationKey.class);
        when(key.getId()).thenReturn(UUID.randomUUID());
        when(key.getValue()).thenReturn(null);

        assertThrows(OperationKeyNotFoundException.class, () -> manager.validated(key));

        verify(cacheService, never()).search(anyString());
    }
}