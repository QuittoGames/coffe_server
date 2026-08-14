package com.quitto.server.application.controllers.REST.AIProvaider;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quitto.server.application.dto.AIProvider.AIModelDTO;
import com.quitto.server.application.dto.AIProvider.AIModelRequestDTO;
import com.quitto.server.application.dto.AIProvider.AIProviderDTO;
import com.quitto.server.application.dto.AIProvider.AIProviderRequestDTO;
import com.quitto.server.infrastructure.interfaces.AI.AIProvaiderPort;

@RestController
@RequestMapping("/coffee/api/v1/ai/provider")
public class AIProvaiderController {

    private final AIProvaiderPort provaiderAdpiter;

    public AIProvaiderController(AIProvaiderPort provaiderAdpiter) {
        this.provaiderAdpiter = provaiderAdpiter;
    }

    @GetMapping("/models/{model}")
    public AIModelDTO getAIModels(@PathVariable("model") AIModelRequestDTO model) {
        return AIModelDTO.from(provaiderAdpiter.getModel(model.model()));
    }

    @GetMapping("/models")
    public List<AIModelDTO> getAllAIModels() {
        return provaiderAdpiter.getAllModels().stream()
                .map(AIModelDTO::from)
                .toList();
    }

    @GetMapping("/provaiders")
    public List<AIProviderDTO> getAllProvaiders() {
        return provaiderAdpiter.getAllProviders().stream()
                .map(AIProviderDTO::from)
                .toList();
    }

    @GetMapping("/provaiders/{provaider}")
    public AIProviderDTO findProvaider(@PathVariable("provaider") AIProviderRequestDTO provaider) {
        return AIProviderDTO.from(provaiderAdpiter.findProvider(provaider.provaider()));
    }
}
