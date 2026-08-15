package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class ZhipuAIProvider extends BaseProvider {

    {
        setEnvId("ZHIPU_AI");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.ZHIPU_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.ZHIPU_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://open.bigmodel.cn/api/paas/v4";
    }
}