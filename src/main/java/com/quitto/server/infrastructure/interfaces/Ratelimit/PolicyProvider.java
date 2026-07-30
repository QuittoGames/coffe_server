package com.quitto.server.infrastructure.interfaces.Ratelimit;

import com.quitto.server.domain.enums.RateLimitPolicy;

import io.github.bucket4j.BucketConfiguration;

public interface PolicyProvider {
    BucketConfiguration getConfiguration(RateLimitPolicy policy);
}
