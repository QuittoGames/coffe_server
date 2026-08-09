package com.quitto.server.infrastructure.CoffeAgent.protocol;

import com.quitto.server.infrastructure.CoffeAgent.content.WebContentProtocol;

public record RequestAgent(
    String token,
    String idempotencyKey,
    WebContentProtocol content
) {

}
