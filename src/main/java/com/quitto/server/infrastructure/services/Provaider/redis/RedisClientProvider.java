package com.quitto.server.infrastructure.services.Provaider.redis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.quitto.server.domain.interfaces.Database.DatabaseClientProvider;
import com.quitto.server.infrastructure.Adpter.in.RedisClientConnectionAdpter;
import com.quitto.server.domain.Database.redis.RedisClientInstace;
import com.quitto.server.infrastructure.config.redis.RedisProperties;
import com.quitto.server.infrastructure.config.redis.Codec.StringByteArrayCodec;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

@Component
@ConditionalOnProperty(prefix = "coffee.redis", name = "enabled", havingValue = "true", matchIfMissing = false)
public class RedisClientProvider implements DatabaseClientProvider<RedisClientConnectionAdpter> {

    private final Map<String, RedisClientConnectionAdpter> clients = new HashMap<>();

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
                new RedisClientConnectionAdpter(connection)
            );
        }
    }

    @Override
    public RedisClientConnectionAdpter get(String name) {
        RedisClientConnectionAdpter adapter = clients.get(name);
        if (adapter == null) {
            throw new IllegalArgumentException("Redis client not found: " + name);
        }
        return adapter;
    }
}
