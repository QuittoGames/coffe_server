package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;

import org.springframework.stereotype.Service;

/**
 * AWS Bedrock — a listagem de modelos usa a operação {@code ListFoundationModels}
 * do AWS SDK com assinatura SigV4, não um endpoint HTTP público (ver TODO em
 * {@link #fetchModelsFromApi()}).
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

    @Override
    protected String modelsUrl() {
        // Não há endpoint HTTP público de listagem no Bedrock; mantido apenas para
        // documentar a base (a listagem real é ListFoundationModels via SDK).
        return "https://bedrock.{REGION}.amazonaws.com/models";
    }

    @Override
    public void fetchModelsFromApi() {
        // TODO: ListFoundationModels exige AWS SDK + SigV4 (aws-sdk-java) — fora do
        // escopo do BaseProvider (HTTP puro). O erro explícito evita request de rede
        // que falharia com 401/403 silencioso.
        throw new ProviderException(
                "AWS Bedrock requer AWS SDK (SigV4) para ListFoundationModels — "
                        + "fetchModelsFromApi() ainda não implementado para '" + getName() + "'.");
    }
}
