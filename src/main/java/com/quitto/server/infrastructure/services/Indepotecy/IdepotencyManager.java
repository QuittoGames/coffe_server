package com.quitto.server.infrastructure.services.Indepotecy;

import java.security.InvalidKeyException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.exception.OperationKeyIdNotFoundException;
import com.quitto.server.domain.exception.OperationKeyNotFoundException;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.infrastructure.services.Chace.CacheService;

@Service
public class IdepotencyManager {
    private final CacheService cacheService;

    public IdepotencyManager(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void register(OperationKey key){
        Objects.requireNonNull(key);

        if(!validateStateKey(key)){

        }
    }

    public boolean validadatedIndepotetion(OperationKey Key) throws InvalidKeyException{
        Objects.requireNonNull(Key);

        String idOfKey = String.valueOf(Key.getId());
        if (idOfKey == null || idOfKey.isBlank()) {
            throw new OperationKeyIdNotFoundException("Operation key id is null or blank");
        }

        String valueOfKey = String.valueOf(Key.getValue());
        if (valueOfKey == null || valueOfKey.isBlank()) {
            throw new OperationKeyNotFoundException("Operation key value is null or blank");
        }

        Optional<String> redisKey = cacheService.search(idOfKey);

        return redisKey.isPresent();
    }

    public boolean validateStateKey(OperationKey key){
        return key.isValid();
    }

    public void cacheadKey(OperationKey key){
        cacheService.createCache(key.getId().toString(), key.getValue().toString());
    }
}
