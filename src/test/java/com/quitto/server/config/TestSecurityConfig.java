package com.quitto.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.infrastructure.services.Auth.Token.Jtw.JwtTokenService;

@Configuration
public class TestSecurityConfig {

    @Bean
    @Primary
    public TokenService<Long> primaryTokenService(JwtTokenService jwtTokenService) {
        return jwtTokenService;
    }
}
