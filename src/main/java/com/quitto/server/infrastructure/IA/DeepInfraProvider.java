package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class DeepInfraProvider extends BaseProvider {

    {
        setEnvId("DEEPINFRA");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.DEEPINFRA;
    }

    @Override
    public String getName() {
        return ServiceProvider.DEEPINFRA.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.deepinfra.com/v1";
    }

    @Override
    protected String modelsUrl() {
        // GET https://api.deepinfra.com/models/list — a listagem vive fora de /v1
        // (o default <base>/models apontaria para /v1/models, que não existe).
        // O parser padrão cobre a raiz "models" da resposta.
        return "https://api.deepinfra.com/models/list";
    }
}