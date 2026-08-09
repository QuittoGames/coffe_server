package com.quitto.server.infrastructure.CoffeAgent.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.quitto.server.infrastructure.CoffeAgent.content.Actions.AgentActions;

public record WebContentProtocol(
    AgentActions action,
    JsonNode content
) {
}
