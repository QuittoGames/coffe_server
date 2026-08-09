package com.quitto.server.application.dto.CoffeAgent;

import com.quitto.server.infrastructure.CoffeAgent.content.WebContentProtocol;

public record RequestAgentDTO(
    String token,
    String idempotencyKey,
    WebContentProtocol content
) {

}
