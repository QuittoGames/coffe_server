package com.quitto.server.infrastructure.config.redis;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.quitto.server.domain.Database.redis.RedisClientInstace;

@ConfigurationProperties(prefix = "coffee.redis")
public class RedisProperties {

    private List<RedisClientInstace> instances;

    public List<RedisClientInstace> getInstances() {
        return instances;
    }

    public void setInstances(List<RedisClientInstace> instances) {
        this.instances = instances;
    }
}
