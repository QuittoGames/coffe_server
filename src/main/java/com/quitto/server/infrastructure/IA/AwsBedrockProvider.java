package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

/**
 * AWS Bedrock — a listagem de modelos usa assinatura SigV4 (AWS SDK) e não o
 * header {@code Authorization: Bearer}. Este provedor documenta a URL base;
 * a autenticação real deve ser implementada fora da {@link BaseProvider}.
 */
@Service
public class AwsBedrockProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.AWS_BEDROCK;
    }

    @Override
    public String getName() {
        return ServiceProvider.AWS_BEDROCK.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://bedrock.{REGION}.amazonaws.com";
    }

    @Override
    protected boolean usesBearer() {
        return false;
    }
}
