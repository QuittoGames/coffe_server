package com.quitto.server.domain.valueobject.IdempotencyKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import com.quitto.server.domain.interfaces.OperationKey.OperationKey;

public class IdempotencyKey implements OperationKey {

    private UUID id;
    private UUID value;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean valid = false;

    @Override
    public UUID getValue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValue'");
    }

    @Override
    public LocalDateTime getCreationDate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCreationDate'");
    }

    @Override
    public Instant createdAt() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createdAt'");
    }

    @Override
    public Instant expiresAt() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'expiresAt'");
    }

    @Override
    public boolean isExpired() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isExpired'");
    }

    public boolean validatedKey(){
        return valid;
    }

}
