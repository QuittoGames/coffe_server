package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class LeptonAIProvider extends BaseProvider {

    {
        setEnvId("LEPTON_AI");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.LEPTON_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.LEPTON_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://llm-proxy.lepton.ai/v1";
    }
}
