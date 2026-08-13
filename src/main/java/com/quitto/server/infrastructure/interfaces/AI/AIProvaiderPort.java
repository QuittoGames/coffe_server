package com.quitto.server.infrastructure.interfaces.AI;

import java.util.List;

import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.models.IA.AIModel;

public interface AIProvaiderPort {
    List<AIProvider> getAllProviders();
    List<AIModel> getAllModels();
    AIModel getModel(String modelId);
    AIProvider findProvider(String providerString);
}
