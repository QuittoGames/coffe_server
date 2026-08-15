package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;

import org.springframework.stereotype.Service;

/**
 * Oracle Cloud Infrastructure (OCI) Generative AI — listagem de modelos.
 * <p>A assinatura OCI (RSA-SHA256 com tenancy/user/fingerprint) exige o SDK
 * {@code oci-java-sdk-generativeai}; a assinatura manual é complexa demais para
 * este escopo (desvio documentado — ver docs/specs/ai-provider-service.md, SC-003).
 * Sem credenciais configuradas a falha é sanitizada; com credenciais, a mensagem
 * indica o requisito do SDK. Nenhuma informação interna é exposta.</p>
 */
@Service
public class OCIGenerativeAIProvider extends BaseProvider {

    {
        setEnvId("OCI_GENERATIVE_AI");
    }

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
        return "https://inference.generativeai.us-chicago-1.oci.oraclecloud.com";
    }

    @Override
    public boolean requiresKey() {
        return false;
    }

    @Override
    public boolean usesBearer() {
        return false;
    }

    @Override
    public void fetchModelsFromApi() {
        String tenancy = System.getenv("OCI_TENANCY_OCID");
        String user = System.getenv("OCI_USER_OCID");
        String fingerprint = System.getenv("OCI_API_KEY_FINGERPRINT");
        if (tenancy == null || tenancy.isBlank()
                || user == null || user.isBlank()
                || fingerprint == null || fingerprint.isBlank()) {
            throw new ProviderException(
                    "Credenciais OCI não configuradas para o provedor 'OCI_GENERATIVE_AI' "
                            + "(env: OCI_TENANCY_OCID/OCI_USER_OCID/OCI_API_KEY_FINGERPRINT).");
        }
        throw new ProviderException(
                "A listagem de modelos do provedor 'OCI_GENERATIVE_AI' requer o SDK oci-java-sdk-generativeai (assinatura OCI).");
    }
}