package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class MoonshotAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.MOONSHOT_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.MOONSHOT_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.moonshot.cn/v1";
    }
}