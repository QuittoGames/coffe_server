package com.quitto.server.application.controllers.WebSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.quitto.server.application.dto.CoffeAgent.RequestAgentDTO;
import com.quitto.server.infrastructure.CoffeAgent.content.Actions.AgentActions;
import com.quitto.server.infrastructure.CoffeAgent.protocol.RequestAgent;
import com.quitto.server.infrastructure.CoffeAgent.protocol.ResponseContentAgent;
import com.quitto.server.infrastructure.services.CoffeAgent.CoffeAgentRequestManager;
import com.quitto.server.infrastructure.services.CoffeAgent.CoffeAgentService;

@Controller
public class AgentWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(AgentWebSocketController.class);

    private final CoffeAgentService agentService;
    private final CoffeAgentRequestManager requestManager;

    public AgentWebSocketController(CoffeAgentService agentService, CoffeAgentRequestManager requestManager) {
        this.agentService = agentService;
        this.requestManager = requestManager;
    }

    @MessageMapping("/agent")
    @SendTo("/queue/coffee-agent")
    public ResponseContentAgent sendRequest(RequestAgentDTO requestDto) {
        try {
            String token = agentService.getServerToken();
            RequestAgent internalRequest = requestManager.request(token,requestDto);

            // Extract the action from the request content
            AgentActions action = internalRequest.content().action();
            return new ResponseContentAgent("success", "", action, "processed");
        } catch (IllegalArgumentException e) {
            logger.error("Failed to process agent websocket request", e);
            return new ResponseContentAgent("error", "", AgentActions.ERROR, "");
        }
    }
}
