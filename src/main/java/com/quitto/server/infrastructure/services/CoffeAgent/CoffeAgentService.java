package com.quitto.server.infrastructure.services.CoffeAgent;

import org.springframework.stereotype.Service;

@Service
public class CoffeAgentService {
    public String getEnvKey(String value){
        return "key_temp";
    }
}
