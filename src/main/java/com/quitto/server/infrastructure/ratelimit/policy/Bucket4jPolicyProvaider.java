package com.quitto.server.infrastructure.config.ratelimit;

import java.time.Duration;

import com.quitto.server.domain.enums.RateLimitPolicy;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;

public class Bucket4jPolicyProvaider {
    public BucketConfiguration getConfiguration(RateLimitPolicy policy) {
        return switch (policy) {
            case LOGIN -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(5, Duration.ofMinutes(1)))
                .build();
            case REGISTER -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(3, Duration.ofMinutes(1)))
                .build();
            case API -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(100, Duration.ofMinutes(1)))
                .build();
            case UPLOAD -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(10, Duration.ofMinutes(1)))
                .build();
        };
    }

}
