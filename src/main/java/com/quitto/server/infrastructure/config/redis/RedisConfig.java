package com.quitto.server.infrastructure.config.redis;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

}
