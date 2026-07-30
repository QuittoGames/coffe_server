package com.quitto.server.infrastructure.Adpter.Auth;

import java.util.Optional;

import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.infrastructure.services.Auth.Token.Jtw.JwtTokenService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAdapter implements TokenService<Long>{
    private final JwtTokenService service;

    public JwtTokenAdapter(JwtTokenService service){
        this.service = service;
    }

    @Override
    public String generateToken(Long id){
        return service.generateToken(id);
    }

    @Override
    public boolean verifyToken(String token){
        return service.verifyToken(token);
    }

    @Override
    public Optional<Long> extractIdSubject(String token){
        return service.extractIdSubject(token);
    }

}
