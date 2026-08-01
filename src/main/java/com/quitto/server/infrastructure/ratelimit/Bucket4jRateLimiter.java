package com.quitto.server.infrastructure.ratelimit;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.RateLimitPolicy;
import com.quitto.server.infrastructure.interfaces.Ratelimit.PolicyProvider;
import com.quitto.server.infrastructure.interfaces.Ratelimit.RateLimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;


@Service
@ConditionalOnBean(ProxyManager.class)
public class Bucket4jRateLimiter implements RateLimit {

    private final ProxyManager<String> proxyManager;
    private final PolicyProvider provider;

    public Bucket4jRateLimiter(ProxyManager<String> proxyManager,PolicyProvider provider) {
        this.proxyManager = proxyManager;
        this.provider = provider;
    }

    @Override
    public boolean tryConsume(String key, RateLimitPolicy policy) {
        BucketConfiguration configuration = provider.getConfiguration(policy);

        Bucket bucket = Bucket.builder()
            .addLimit(limit -> limit
                .capacity(100)
                .refillGreedy(100, Duration.ofMinutes(1))
            )
            .build();

        return bucket.tryConsume(1);
    }
}
