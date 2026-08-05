package com.quitto.server.infrastructure.services.IA.Provaiders;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.infrastructure.services.IA.Provaiders.Reagistry.AIProviderRegistry;

@Service
public class ProvaiderIAService {
    private final AIProviderRegistry registry;

    public ProvaiderIAService(AIProviderRegistry registry) {
        this.registry = registry;
    }

    public void getModels(){

    }

}
