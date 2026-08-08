package com.quitto.server.infrastructure.services.DatabaseProvaider.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;

import com.quitto.server.domain.interfaces.Database.DatabaseClientProvider;
import com.quitto.server.domain.Database.redis.RedisClientInstace;
import com.quitto.server.infrastructure.Adapters.in.RedisClientConnectionAdapter;
import com.quitto.server.infrastructure.config.redis.RedisProperties;
import com.quitto.server.infrastructure.config.redis.Codec.StringByteArrayCodec;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

import jakarta.annotation.PreDestroy;

@Component
@ConditionalOnProperty(prefix = "coffee.redis", name = "enabled", havingValue = "true", matchIfMissing = false)
public class RedisClientProvider implements DatabaseClientProvider<RedisClientConnectionAdapter,RedisProperties> {

    /** Handles Lettuce baratos (sem conexão de rede) por nome de instância. */
    private final Map<String, RedisClient> clients;

    /** Conexões criadas sob demanda no primeiro uso e cacheadas. */
    private final Map<String, RedisClientConnectionAdapter> connections;

    public RedisClientProvider(RedisProperties properties) {
        this.clients = buildClients(properties);
        this.connections = new ConcurrentHashMap<>();
    }

    private Map<String, RedisClient> buildClients(RedisProperties properties) {
        Map<String, RedisClient> clients = new HashMap<>();

        if (properties.getInstances() == null) {
            return clients;
        }

        for (RedisClientInstace clientConfig : properties.getInstances()) {
            clients.put(
                clientConfig.getName(),
                RedisClient.create(buildUri(clientConfig))
            );
        }
        return clients;
    }

    /**
     * Monta a {@link RedisURI} da instância. Usa o Builder (em vez de string
     * "redis://host:port") para suportar senha e TLS sem problemas de
     * URL-encoding (ex.: senhas com '@' ou ':').
     */
    private RedisURI buildUri(RedisClientInstace config) {
        RedisURI.Builder builder = RedisURI.Builder.redis(config.getHost(), config.getPort());

        if (config.getPassword() != null && !config.getPassword().isBlank()) {
            builder.withPassword(config.getPassword());
        }
        if (config.isUseSsl()) {
            builder.withSsl(true);
        }

        return builder.build();
    }

    @Override
    public Map<String, RedisClientConnectionAdapter> getProvaiders(RedisProperties properties) {
        return connections;
    }

    @Override
    public RedisClientConnectionAdapter getAdpterConnector(String name) {
        return connections.computeIfAbsent(name, this::connect);
    }

    private RedisClientConnectionAdapter connect(String name) {
        RedisClient client = clients.get(name);
        if (client == null) {
            throw new IllegalArgumentException("Redis client not found: " + name);
        }

        StatefulRedisConnection<String, byte[]> connection = client.connect(
            new StringByteArrayCodec()
        );

        return new RedisClientConnectionAdapter(connection,name);
    }

    public StatefulRedisConnection<String, byte[]> getConnection(String name) throws IllegalArgumentException,DataAccessResourceFailureException{
        if (name.isBlank()) throw new IllegalArgumentException("Name of client is required for get Redis Client: " + name);

        StatefulRedisConnection<String, byte[]> connection = getAdpterConnector(name).getConnection();

        if (connection == null|| !connection.isOpen()){
            throw new DataAccessResourceFailureException("Redis connection unavailable");
        }

        return connection;
    }

    /** Fecha conexões e clients no shutdown do Spring para evitar vazamento de threads Netty. */
    @PreDestroy
    public void shutdown() {
        connections.values().forEach(RedisClientConnectionAdapter::close);
        clients.values().forEach(client -> client.shutdown(0, 1, TimeUnit.SECONDS));
    }
}
