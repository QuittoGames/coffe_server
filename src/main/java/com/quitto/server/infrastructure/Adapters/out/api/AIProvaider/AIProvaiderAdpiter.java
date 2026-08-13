package com.quitto.server.infrastructure.Adapters.out.api.AIProvaider;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.infrastructure.interfaces.AI.AIProvaiderPort;
import com.quitto.server.infrastructure.services.IA.Provaiders.ProvaiderIAService;

@Service
public class AIProvaiderAdpiter implements AIProvaiderPort {
    private final ProvaiderIAService service;

    public AIProvaiderAdpiter(ProvaiderIAService service){
        this.service = service;
    }

    @Override
    public List<AIProvider> getAllProviders(){
        return service.getAllProviders();
    }

    @Override
    public List<AIModel> getAllModels(){
        return service.getAllModels();
    }

    @Override
    public AIModel getModel(String modelId){
        return service.getModel(modelId);
    }

    @Override
    public AIProvider findProvider(String providerString){
        ServiceProvider provider = ServiceProvider.valueOf(providerString.toUpperCase());
        return service.findProvider(provider);
    }

}
