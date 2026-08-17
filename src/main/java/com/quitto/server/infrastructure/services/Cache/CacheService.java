package com.quitto.server.infrastructure.services.Cache;

import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.exception.Cache.CacheKeyNotFoundException;
import com.quitto.server.domain.exception.OperationKey.InvalidIdempotencyKeyException;
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
        if (key == null || key.isBlank()) {
            throw new InvalidIdempotencyKeyException("Cache key must not be null or blank");
        }

        String value = getConnection().search(key);

        if (value == null) {
            return Optional.empty();
        }

        return Optional.of(value);
    }

    /**
     * Busca uma chave no cache exigindo a presença do valor.
     *
     * <p>Diferente de {@link #search(String)} — onde ausência (cache miss) é
     * circunstância normal e retorna {@link Optional#empty()} — este método
     * trata cache miss como erro, lançando {@link CacheKeyNotFoundException}.</p>
     *
     * @param key chave a buscar (não pode ser null ou blank)
     * @return o valor armazenado no cache
     * @throws InvalidIdempotencyKeyException se a chave for null ou blank
     * @throws CacheKeyNotFoundException se a chave não existir no cache
     */
    public String searchRequired(String key) throws InvalidIdempotencyKeyException {
        return search(key)
                .orElseThrow(() -> new CacheKeyNotFoundException("Cache key not found: " + key));
    }
}
