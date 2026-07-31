package com.quitto.server.infrastructure.Adapters.out.Auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TokenAdapter {
    public static UsernamePasswordAuthenticationToken transferData(String name , String password){
        return new UsernamePasswordAuthenticationToken(name, password);
    }
}
