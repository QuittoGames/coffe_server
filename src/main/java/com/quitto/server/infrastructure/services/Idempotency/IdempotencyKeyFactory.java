package com.quitto.server.infrastructure.services.Idempotency;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.interfaces.OperationKey.OperationKeyFactory;
import com.quitto.server.domain.valueobject.IdempotencyKey.IdempotencyKey;

@Service
public class IdempotencyKeyFactory implements OperationKeyFactory {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);

    @Override
    public OperationKey create() {
        return create(DEFAULT_TTL);
    }

    @Override
    public OperationKey create(Duration ttl) {
        Duration effectiveTtl = ttl == null ? DEFAULT_TTL : ttl;
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return new IdempotencyKey(
            UUID.randomUUID(),
            UUID.randomUUID(),
            now,
            now.plus(effectiveTtl)
        );
    }

}
