package com.quitto.server.domain.interfaces.OperationKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public interface OperationKey {
    UUID getValue();

    LocalDateTime getCreationDate();

    Instant createdAt();

    Instant expiresAt();

    boolean isExpired();
}
