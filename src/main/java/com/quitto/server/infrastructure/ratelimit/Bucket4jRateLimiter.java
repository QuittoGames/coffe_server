package com.quitto.server.infrastructure.ratelimit;

import com.quitto.server.domain.enums.RateLimitPolicy;
import com.quitto.server.domain.interfaces.Database.DatabaseClientProvider;
import com.quitto.server.infrastructure.Adapters.in.RedisClientConnectionAdapter;
import com.quitto.server.infrastructure.config.redis.RedisProperties;
import com.quitto.server.infrastructure.interfaces.Ratelimit.PolicyProvider;
import com.quitto.server.infrastructure.interfaces.Ratelimit.RateLimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.api.StatefulRedisConnection;

import org.springframework.dao.DataAccessResourceFailureException;

/**
 * Rate limiter distribuído (Redis via bucket4j).
 *
 * <p>A conexão com o Redis é criada de forma <b>lazy</b> — apenas na primeira
 * chamada de {@link #tryConsume} — para que o boot do servidor não dependa do
 * Redis estar no ar.
 */
public class Bucket4jRateLimiter implements RateLimit {

    private final DatabaseClientProvider<RedisClientConnectionAdapter, RedisProperties> provider;
    private final PolicyProvider policyProvider;

    private volatile ProxyManager<String> proxyManager;

    public Bucket4jRateLimiter(
            DatabaseClientProvider<RedisClientConnectionAdapter, RedisProperties> provider,
            PolicyProvider policyProvider) {
        this.provider = provider;
        this.policyProvider = policyProvider;
    }

    private ProxyManager<String> proxyManager() {
        ProxyManager<String> current = proxyManager;
        if (current == null) {
            synchronized (this) {
                current = proxyManager;
                if (current == null) {
                    RedisClientConnectionAdapter adapter = provider.getAdpterConnector("rate-limit");
                    StatefulRedisConnection<String, byte[]> connection = adapter.getConnection();
                    if (connection == null || !connection.isOpen()) {
                        throw new DataAccessResourceFailureException("Redis connection unavailable");
                    }
                    current = LettuceBasedProxyManager.builderFor(connection.async()).build();
                    proxyManager = current;
                }
            }
        }
        return current;
    }

    @Override
    public boolean tryConsume(String key, RateLimitPolicy policy) {
        BucketConfiguration configuration = policyProvider.getConfiguration(policy);
        Bucket bucket = proxyManager().getProxy(key, () -> configuration);
        return bucket.tryConsume(1);
    }
}
