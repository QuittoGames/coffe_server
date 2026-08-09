package com.quitto.server.infrastructure.services.CoffeAgent;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.quitto.server.application.dto.CoffeAgent.RequestAgentDTO;
import com.quitto.server.infrastructure.CoffeAgent.protocol.RequestAgent;

@Service
public class CoffeAgentRequestManager {
    public RequestAgent request(String token ,RequestAgentDTO data){
        Objects.requireNonNull(token);
        Objects.requireNonNull(data.idempotencyKey());
        Objects.requireNonNull(data.content());

        return new RequestAgent(data.token(), data.idempotencyKey(), data.content());
    }
}
