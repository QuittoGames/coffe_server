package com.quitto.server.infrastructure.services.Idempotency;

import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.exception.InvalidIdempotencyKeyException;
import com.quitto.server.domain.exception.OperationKeyIdNotFoundException;
import com.quitto.server.domain.exception.OperationKeyNotFoundException;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.interfaces.OperationKey.OperationKeyFactory;
import com.quitto.server.domain.interfaces.OperationKey.OperationKeyManager;
import com.quitto.server.infrastructure.services.Cache.CacheService;

@Service
@ConditionalOnProperty(prefix = "coffee.redis", name = "enabled", havingValue = "true")
public class IdempotencyManager implements OperationKeyManager{
    private final CacheService cacheService;
    private final OperationKeyFactory factory;

    public IdempotencyManager(CacheService cacheService,OperationKeyFactory factory) {
        this.cacheService = cacheService;
        this.factory = factory;
    }

    @Override
    public void register(OperationKey key){
        Objects.requireNonNull(key);

        if(!validateStateKey(key)){
            key = factory.create();
        }

        cacheKey(key);
    }

    @Override
    public boolean validateStateKey(OperationKey key){
        return key.isValid();
    }

    public void cacheKey(OperationKey key){
        cacheService.createCache(key.getId().toString(), key.getValue().toString());
    }

    @Override
    public boolean validated(OperationKey key) throws InvalidIdempotencyKeyException{
        Objects.requireNonNull(key);

        String idOfKey = String.valueOf(key.getId());
        if (idOfKey == null || idOfKey.isBlank()) {
            throw new OperationKeyIdNotFoundException("Operation key id is null or blank");
        }

        String valueOfKey = String.valueOf(key.getValue());
        if (valueOfKey == null || valueOfKey.isBlank()) {
            throw new OperationKeyNotFoundException("Operation key value is null or blank");
        }

        Optional<String> redisKey = cacheService.search(idOfKey);

        return redisKey.isPresent();
    }
}

