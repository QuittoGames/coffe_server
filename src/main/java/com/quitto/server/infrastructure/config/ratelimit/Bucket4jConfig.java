package com.quitto.server.infrastructure.config.ratelimit;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;

@Configuration
@ConditionalOnProperty(name = "rate.limit.redis.enabled", havingValue = "true", matchIfMissing = true)
public class Bucket4jConfig {

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(100, Duration.ofMinutes(1)))
                .build();
    }

    @Bean
    public ProxyManager<String> proxyManager(RedisAsyncCommands<String, byte[]> commands) {
        return LettuceBasedProxyManager
                .builderFor(commands)
                .build();
    }
}
