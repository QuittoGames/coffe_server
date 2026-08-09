package com.quitto.server.infrastructure.CoffeAgent.protocol;

import com.quitto.server.infrastructure.CoffeAgent.content.Actions.AgentActions;

public record ResponseContentAgent(
    String status,
    String userUse,
    AgentActions action,
    String operation
) {
}

