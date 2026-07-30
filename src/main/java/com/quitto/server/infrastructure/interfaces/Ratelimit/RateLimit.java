package com.quitto.server.infrastructure.interfaces.Ratelimit;

import com.quitto.server.domain.enums.RateLimitPolicy;

public interface RateLimit {
    boolean tryConsume(String key, RateLimitPolicy policy);
}
