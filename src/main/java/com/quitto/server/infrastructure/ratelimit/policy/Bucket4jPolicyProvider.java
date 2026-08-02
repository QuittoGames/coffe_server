package com.quitto.server.infrastructure.ratelimit.policy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.RateLimitPolicy;
import com.quitto.server.infrastructure.interfaces.Ratelimit.PolicyProvider;

import io.github.bucket4j.BucketConfiguration;

/**
 * Default implementation of {@link PolicyProvider}.
 *
 * <p>Currently every {@link RateLimitPolicy} shares the same bucket
 * configuration defined in {@code Bucket4jConfig}. Per-policy limits can be
 * introduced here later without touching the rate limiter.
 *
 * <p>Conditional on {@code coffee.ratelimit.enabled=true}: the {@link
 * BucketConfiguration} bean it depends on only exists when rate limiting is
 * enabled, so this provider must not be created otherwise.
 */
@Service
@ConditionalOnProperty(
    prefix = "coffee.ratelimit",
    name = "enabled",
    havingValue = "true"
)
public class Bucket4jPolicyProvider implements PolicyProvider {

    private final BucketConfiguration defaultConfiguration;

    public Bucket4jPolicyProvider(BucketConfiguration defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }

    @Override
    public BucketConfiguration getConfiguration(RateLimitPolicy policy) {
        return defaultConfiguration;
    }
}
