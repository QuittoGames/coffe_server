package com.quitto.server.application.dto.CoffeAgent;

import com.quitto.server.application.dto.BaseDTO;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.infrastructure.CoffeAgent.content.Actions.AgentActions;

public record ResponseContentAgentDTO(
    OperationKey idempotencyKey,
    String status,
    String userUse,
    AgentActions action,
    String operation
) implements BaseDTO {

}