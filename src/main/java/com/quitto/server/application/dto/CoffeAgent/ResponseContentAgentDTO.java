package com.quitto.server.application.dto.CoffeAgent;

import com.quitto.server.infrastructure.CoffeAgent.content.Actions.AgentActions;

public record ResponseContentAgentDTO(
    String status,
    String userUse,
    AgentActions action,
    String operation
){

}

