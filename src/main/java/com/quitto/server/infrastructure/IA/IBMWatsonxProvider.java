package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

/**
 * IBM watsonx — usa um IAM token como Bearer (obtido de
 * {@code https://iam.cloud.ibm.com/identity/token}) e URL base por região.
 */
@Service
public class IBMWatsonxProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.IBM_WATSONX;
    }

    @Override
    public String getName() {
        return ServiceProvider.IBM_WATSONX.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://{REGION}.ml.cloud.ibm.com/ml";
    }

    @Override
    protected String modelsUrl() {
        // GET /ml/v1/foundation_model_specs?version=YYYY-MM-DD — o parâmetro version
        // é obrigatório. O parser padrão do BaseProvider já cobre a raiz "resources"
        // da resposta.
        return "https://{REGION}.ml.cloud.ibm.com/ml/v1/foundation_model_specs?version=2026-08-05";
    }
}
