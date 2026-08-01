package com.quitto.server.infrastructure.services.Provaider.redis;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;

import com.quitto.server.domain.interfaces.Database.DatabaseClientProvider;
import com.quitto.server.domain.Database.redis.RedisClientInstace;
import com.quitto.server.infrastructure.Adapters.in.RedisClientConnectionAdapter;
import com.quitto.server.infrastructure.config.redis.RedisProperties;
import com.quitto.server.infrastructure.config.redis.Codec.StringByteArrayCodec;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

@Component
@ConditionalOnProperty(prefix = "coffee.redis", name = "enabled", havingValue = "true", matchIfMissing = false)
public class RedisClientProvider implements DatabaseClientProvider<RedisClientConnectionAdapter> {

    private final Map<String, RedisClientConnectionAdapter> clients = new HashMap<>();

    public RedisClientProvider(RedisProperties properties) {
        if (properties.getInstances() == null) {
            return;
        }

        for (RedisClientInstace clientConfig : properties.getInstances()) {
            RedisClient client = RedisClient.create(
                "redis://" + clientConfig.getHost() + ":" + clientConfig.getPort()
            );

            StatefulRedisConnection<String, byte[]> connection = client.connect(
                new StringByteArrayCodec()
            );

            clients.put(
                clientConfig.getName(),
                new RedisClientConnectionAdapter(connection)
            );
        }
    }

    @Override
    public RedisClientConnectionAdapter get(String name) {
        RedisClientConnectionAdapter adapter = clients.get(name);
        if (adapter == null) {
            throw new IllegalArgumentException("Redis client not found: " + name);
        }
        return adapter;
    }

    public StatefulRedisConnection<String, byte[]> getConnection(String name) throws IllegalArgumentException,DataAccessResourceFailureException{
        if (name.isBlank()) throw new IllegalArgumentException("Name of client is required for get Redis Client: " + name);

        RedisClientConnectionAdapter adapter = clients.get(name);
        if (adapter == null) {
            throw new IllegalArgumentException("Redis client not found: " + name);
        }

        StatefulRedisConnection<String, byte[]> connection = adapter.getConnection();

        if (connection == null|| !connection.isOpen()){
            throw new DataAccessResourceFailureException("Redis connection unavailable");
        }

        return connection;
    }
}
