package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class QianfanProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.QIANFAN;
    }

    @Override
    public String getName() {
        return ServiceProvider.QIANFAN.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://qianfan.baidubce.com/v2";
    }
}