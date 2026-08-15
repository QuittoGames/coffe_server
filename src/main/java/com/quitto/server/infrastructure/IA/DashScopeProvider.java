package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class DashScopeProvider extends BaseProvider {

    {
        setEnvId("DASHSCOPE");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.DASHSCOPE;
    }

    @Override
    public String getName() {
        return ServiceProvider.DASHSCOPE.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://dashscope.aliyuncs.com/compatible-mode/v1";
    }
}