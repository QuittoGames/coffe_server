package com.quitto.server.domain.interfaces.OperationKey;

import com.quitto.server.domain.exception.OperationKey.InvalidIdempotencyKeyException;

public interface OperationKeyManager {
    public void register(OperationKey key);
    public boolean validateStateKey(OperationKey key);
    public boolean validated(OperationKey key) throws InvalidIdempotencyKeyException;

}
