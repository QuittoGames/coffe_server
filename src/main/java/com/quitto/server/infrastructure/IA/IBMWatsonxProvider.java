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
}
