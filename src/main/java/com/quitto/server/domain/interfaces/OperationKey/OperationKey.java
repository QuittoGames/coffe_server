package com.quitto.server.domain.interfaces.OperationKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public interface OperationKey {
    UUID getId();

    UUID getValue();

    LocalDateTime getCreationDate();

    Instant createdAt();

    Instant expiresAt();

    boolean isExpired();

    boolean isValid();
}
