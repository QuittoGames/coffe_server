package com.quitto.server.infrastructure.IA;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;
import com.quitto.server.domain.models.IA.AIModel;

/**
 * Amazon Bedrock — lista modelos via {@code GET /foundation-models} assinado
 * com SigV4 (helper local {@link AwsSigV4}, sem SDK AWS — PG-003). O parse usa
 * o formato nativo do Bedrock (raiz {@code modelSummaries}).
 * <p>Credenciais lidas de {@code AWS_ACCESS_KEY_ID}/{@code AWS_SECRET_ACCESS_KEY}/
 * {@code AWS_REGION} (fallback {@code COFFEE_AI_AWS_BEDROCK_*} e região padrão
 * {@code us-east-1}). Ausência de credenciais → falha sanitizada.</p>
 */
@Service
public class AwsBedrockProvider extends BaseProvider {

    private static final String DEFAULT_REGION = "us-east-1";

    {
        setEnvId("AWS_BEDROCK");
    }

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
        return "https://bedrock." + region() + ".amazonaws.com";
    }

    @Override
    public boolean requiresKey() {
        // A chave não é Bearer; as credenciais SigV4 são lidas do ambiente em fetchModelsFromApi()
        return false;
    }

    @Override
    public boolean usesBearer() {
        return false;
    }

    @Override
    public void fetchModelsFromApi() {
        String accessKey = envOrSystem("AWS_ACCESS_KEY_ID", "COFFEE_AI_AWS_BEDROCK_ACCESS_KEY_ID");
        String secretKey = envOrSystem("AWS_SECRET_ACCESS_KEY", "COFFEE_AI_AWS_BEDROCK_SECRET_ACCESS_KEY");
        if (accessKey == null || accessKey.isBlank() || secretKey == null || secretKey.isBlank()) {
            throw new ProviderException(
                    "Credenciais AWS não configuradas para o provedor 'AWS_BEDROCK' "
                            + "(env: AWS_ACCESS_KEY_ID/AWS_SECRET_ACCESS_KEY).");
        }
        String region = region();
        try {
            String host = "bedrock." + region + ".amazonaws.com";
            String payloadHash = AwsSigV4.sha256Hex("");
            var authHeaders = AwsSigV4.authorizationHeaders(
                    accessKey, secretKey, host, region, "bedrock",
                    "GET", "/foundation-models", "", payloadHash, Instant.now());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://" + host + "/foundation-models"))
                    .header("x-amz-date", authHeaders.get("x-amz-date"))
                    .header("Authorization", authHeaders.get("Authorization"))
                    .GET()
                    .build();
            HttpResponse<String> response = http().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new ProviderException(
                        "Falha ao listar modelos do Amazon Bedrock (HTTP " + response.statusCode() + ").");
            }
            JsonNode root = mapper().readTree(response.body());
            List<AIModel> parsed = new ArrayList<>();
            JsonNode summaries = root.path("modelSummaries");
            for (JsonNode node : summaries) {
                String modelId = text(node, "modelId");
                if (modelId == null) {
                    continue;
                }
                parsed.add(new AIModel(
                        modelId,
                        firstNonNull(text(node, "modelName"), modelId),
                        ServiceProvider.AWS_BEDROCK,
                        defaultStreamSupport(),
                        defaultToolsSupport(),
                        defaultReasoningSupport()));
            }
            this.models = parsed;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            String detail = (e.getMessage() == null || e.getMessage().isBlank())
                    ? e.getClass().getSimpleName()
                    : e.getMessage();
            throw new ProviderException(
                    "Falha ao listar modelos do Amazon Bedrock: " + detail, e);
        }
    }

    private static String region() {
        String region = envOrSystem("AWS_REGION", "COFFEE_AI_AWS_BEDROCK_REGION");
        return (region == null || region.isBlank()) ? DEFAULT_REGION : region.trim();
    }

    private static String envOrSystem(String env, String prop) {
        String value = System.getenv(env);
        return (value == null || value.isBlank()) ? System.getProperty(prop) : value;
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value != null && value.isValueNode() && !value.isNull()) ? value.asText() : null;
    }

    private static String firstNonNull(String a, String b) {
        return (a != null && !a.isBlank()) ? a : b;
    }
}