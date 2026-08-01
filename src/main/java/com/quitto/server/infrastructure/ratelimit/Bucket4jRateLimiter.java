package com.quitto.server.infrastructure.ratelimit;

import com.quitto.server.domain.enums.RateLimitPolicy;
import com.quitto.server.infrastructure.interfaces.Ratelimit.PolicyProvider;
import com.quitto.server.infrastructure.interfaces.Ratelimit.RateLimit;
import com.quitto.server.infrastructure.services.Provaider.redis.RedisClientProvider;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.api.async.RedisAsyncCommands;

/**
 * Rate limiter distribuído (Redis via bucket4j).
 *
 * <p>A conexão com o Redis é criada de forma <b>lazy</b> — apenas na primeira
 * chamada de {@link #tryConsume} — para que o boot do servidor não dependa do
 * Redis estar no ar.
 */
public class Bucket4jRateLimiter implements RateLimit {

    private final RedisClientProvider provider;
    private final PolicyProvider policyProvider;

    private volatile ProxyManager<String> proxyManager;

    public Bucket4jRateLimiter(RedisClientProvider provider, PolicyProvider policyProvider) {
        this.provider = provider;
        this.policyProvider = policyProvider;
    }

    private ProxyManager<String> proxyManager() {
        ProxyManager<String> current = proxyManager;
        if (current == null) {
            synchronized (this) {
                current = proxyManager;
                if (current == null) {
                    RedisAsyncCommands<String, byte[]> commands = provider
                            .getConnection("rate-limit")
                            .async();
                    current = LettuceBasedProxyManager.builderFor(commands).build();
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
