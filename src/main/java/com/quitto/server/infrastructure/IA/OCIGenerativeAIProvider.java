package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;

import org.springframework.stereotype.Service;

/**
 * Oracle Cloud Infrastructure Generative AI — a autenticação usa assinatura
 * de requisição OCI (IAM), não um header estático. A assinatura real exige o
 * SDK {@code oci-java-sdk-generativeai} (ver TODO em {@link #fetchModelsFromApi()}).
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

    @Override
    protected String modelsUrl() {
        // GET /20231130/models?compartmentId={ocid} — API versionada; o compartmentId
        // é obrigatório na listagem.
        return "https://generativeai.{REGION}.oci.oraclecloud.com/20231130/models?compartmentId={OCID}";
    }

    @Override
    public void fetchModelsFromApi() {
        // TODO: a listagem do OCI exige assinatura de requisição (OCI signing) via
        // oci-java-sdk-generativeai — fora do escopo do BaseProvider (HTTP puro).
        // O erro explícito evita request de rede que falharia com 401 silencioso.
        throw new ProviderException(
                "OCI Generative AI requer assinatura de requisição OCI (SDK oci-java-sdk-generativeai) — "
                        + "fetchModelsFromApi() ainda não implementado para '" + getName() + "'.");
    }
}
