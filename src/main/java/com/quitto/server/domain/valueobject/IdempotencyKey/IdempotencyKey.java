package com.quitto.server.domain.valueobject.IdempotencyKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import com.quitto.server.domain.interfaces.OperationKey.OperationKey;

public class IdempotencyKey implements OperationKey {

    private UUID id;
    private UUID value;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean valid = false;

    public IdempotencyKey(UUID id, UUID value, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.id = id;
        this.value = value;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public UUID getValue() {
        return this.value;
    }

    @Override
    public LocalDateTime getCreationDate() {
        return this.createdAt;
    }

    @Override
    public Instant createdAt() {
        return this.createdAt.toInstant(ZoneOffset.UTC);
    }

    @Override
    public Instant expiresAt() {
        return this.expiresAt.toInstant(ZoneOffset.UTC);
    }

    @Override
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt());
    }

    @Override
    public boolean isValid() {
        validate();
        return this.valid;
    }

    public void validate() {
        if (!isExpired()) {
            this.valid = true;
        }
    }

    public boolean validatedKey() {
        return valid;
    }

}
