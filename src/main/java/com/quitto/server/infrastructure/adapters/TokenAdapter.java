package com.quitto.server.infrastructure.adapters;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.quitto.server.application.dto.Auth.LoginDTO;

public class TokenAdapter {
    public static UsernamePasswordAuthenticationToken transferData(String name , String password){
        return new UsernamePasswordAuthenticationToken(name, password);
    }
}
