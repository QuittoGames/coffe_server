package com.quitto.server.infrastructure.services.Chace;

import java.security.InvalidKeyException;
import java.util.Objects;

import com.quitto.server.domain.interfaces.Database.Connection;
import com.quitto.server.infrastructure.services.Serializer.JsonSerializer;

public class CacheService {

    private final Connection database;
    private final JsonSerializer serializable;

    public CacheService(Connection database,JsonSerializer serializable) {
        this.database = database;
        this.serializable = serializable;
    }

    public void createCache(String key , String value){
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        byte[] rawData = serializable.serialize(value);

        database.insert(key,rawData);
    }

    public String search(String key) throws InvalidKeyException{
        Objects.requireNonNull(key);

        String value = database.search(key);

        if(value == null){
            throw new InvalidKeyException("Key is not valid , value returend is null");
        }

        return value;
    }
}
