package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

/**
 * Oracle Cloud Infrastructure Generative AI — a autenticação usa assinatura
 * de requisição OCI (IAM), não um header estático. Este provedor documenta a
 * URL base; a assinatura real deve ser implementada fora da {@link BaseProvider}.
 */
@Service
public class OCIGenerativeAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.OCI_GENERATIVE_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.OCI_GENERATIVE_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://generativeai.{REGION}.oci.oraclecloud.com";
    }

    @Override
    protected boolean usesBearer() {
        return false;
    }
}
