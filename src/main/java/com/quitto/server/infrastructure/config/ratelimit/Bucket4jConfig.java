package com.quitto.server.infrastructure.config.ratelimit;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.quitto.server.infrastructure.interfaces.Ratelimit.PolicyProvider;
import com.quitto.server.infrastructure.interfaces.Ratelimit.RateLimit;
import com.quitto.server.infrastructure.ratelimit.Bucket4jRateLimiter;
import com.quitto.server.infrastructure.services.Provaider.redis.RedisClientProvider;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;

@Configuration
@ConditionalOnProperty(
    prefix = "coffee.ratelimit",
    name = "enabled",
    havingValue = "true"
)
public class Bucket4jConfig {

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(100, Duration.ofMinutes(1)))
                .build();
    }

    @Bean
    public RateLimit rateLimit(RedisClientProvider provider, PolicyProvider policyProvider) {
        return new Bucket4jRateLimiter(provider, policyProvider);
    }
}
