package com.quitto.server.infrastructure.services.Cache;

import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.exception.InvalidIdempotencyKeyException;
import com.quitto.server.domain.interfaces.Database.DatabaseClientProvider;
import com.quitto.server.infrastructure.Adapters.in.RedisClientConnectionAdapter;
import com.quitto.server.infrastructure.config.redis.RedisProperties;
import com.quitto.server.infrastructure.services.Serializer.JsonSerializer;

@Service
@ConditionalOnProperty(prefix = "coffee.redis", name = "enabled", havingValue = "true")
public class CacheService {

    private static final String CACHE_INSTANCE = "cache";

    private final DatabaseClientProvider<RedisClientConnectionAdapter, RedisProperties> redisProvider;
    private final JsonSerializer serializable;

    public CacheService(
            DatabaseClientProvider<RedisClientConnectionAdapter, RedisProperties> redisProvider,
            JsonSerializer serializable) {
        this.redisProvider = redisProvider;
        this.serializable = serializable;
    }

    private RedisClientConnectionAdapter getConnection() {
        return redisProvider.getAdpterConnector(CACHE_INSTANCE);
    }

    public void createCache(String key , String value){
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        byte[] rawData = serializable.serialize(value);

        getConnection().insert(key,rawData);
    }

    public Optional<String> search(String key) throws InvalidIdempotencyKeyException{
        Objects.requireNonNull(key);

        String value = getConnection().search(key);

        if(value == null){
            throw new InvalidIdempotencyKeyException("Key is not valid , value returend is null");
        }

        return Optional.of(value);
    }
}
