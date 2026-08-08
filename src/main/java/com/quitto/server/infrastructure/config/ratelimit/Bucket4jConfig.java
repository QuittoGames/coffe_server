package com.quitto.server.infrastructure.config.ratelimit;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.quitto.server.domain.interfaces.Database.DatabaseClientProvider;
import com.quitto.server.infrastructure.Adapters.in.RedisClientConnectionAdapter;
import com.quitto.server.infrastructure.config.redis.RedisProperties;
import com.quitto.server.infrastructure.interfaces.Ratelimit.PolicyProvider;
import com.quitto.server.infrastructure.interfaces.Ratelimit.RateLimit;
import com.quitto.server.infrastructure.ratelimit.Bucket4jRateLimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;

@Configuration
@ConditionalOnProperty(
    prefix = "coffee.ratelimit",
    name = "enabled",
    havingValue = "true"
)
public class Bucket4jConfig {

    @Bean("loginBucket")
    public BucketConfiguration loginBuckeConfig() {
        return BucketConfiguration.builder()
            .addLimit(Bandwidth.simple(5, Duration.ofMinutes(1)))
            .addLimit(Bandwidth.simple(5000, Duration.ofHours(1)))
            .build();
    }

    @Bean("apiBucket")
    public BucketConfiguration apiBucketConfig() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(5, Duration.ofMinutes(1)))
                .build();
    }

    @Bean("uploadBucket")
    public BucketConfiguration uploadBucketConfig() {
        return BucketConfiguration.builder()
            .addLimit(Bandwidth.simple(5, Duration.ofMinutes(1)))
            .addLimit(Bandwidth.simple(100, Duration.ofHours(1)))
            .build();
    }

    @Bean
    public RateLimit rateLimit(
            DatabaseClientProvider<RedisClientConnectionAdapter, RedisProperties> provider,
            PolicyProvider policyProvider) {
        return new Bucket4jRateLimiter(provider, policyProvider);
    }
}
