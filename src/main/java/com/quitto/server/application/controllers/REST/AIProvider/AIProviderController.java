package com.quitto.server.application.controllers.REST.AIProvider;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quitto.server.application.dto.AIProvider.AIModelDTO;
import com.quitto.server.application.dto.AIProvider.AIModelRequestDTO;
import com.quitto.server.application.dto.AIProvider.AIProviderDTO;
import com.quitto.server.application.dto.AIProvider.AIProviderRequestDTO;
import com.quitto.server.application.interfaces.IA.AIProviderPort;

/**
 * REST controller do ecossistema de provedores de IA.
 *
 * <p>Apenas traduz requisições HTTP em chamadas à porta de aplicação
 * {@link AIProviderPort} — nenhuma regra de negócio vive aqui.</p>
 *
 * <p>As URLs mantêm o sufixo "provaiders" (typo histórico) por compatibilidade
 * de API — renomear exige migração de clientes.</p>
 */
@RestController
@RequestMapping("/coffee/api/v1/ai/provider")
public class AIProviderController {

    private final AIProviderPort providerPort;

    public AIProviderController(AIProviderPort providerPort) {
        this.providerPort = providerPort;
    }

    @GetMapping("/models/{model}")
    public AIModelDTO getAIModels(@PathVariable("model") AIModelRequestDTO model) {
        return AIModelDTO.from(providerPort.getModel(model.model()));
    }

    @GetMapping("/models")
    public List<AIModelDTO> getAllAIModels() {
        return providerPort.getAllModels().stream()
                .map(AIModelDTO::from)
                .toList();
    }

    @GetMapping("/provaiders")
    public List<AIProviderDTO> getAllProvaiders() {
        return providerPort.getAllProviders().stream()
                .map(AIProviderDTO::from)
                .toList();
    }

    @GetMapping("/provaiders/{provaider}")
    public AIProviderDTO findProvaider(@PathVariable("provaider") AIProviderRequestDTO provaider) {
        return AIProviderDTO.from(providerPort.findProvider(provaider.provaider()));
    }
}