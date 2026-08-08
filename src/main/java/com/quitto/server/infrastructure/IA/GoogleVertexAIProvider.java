package com.quitto.server.infrastructure.IA;

import com.fasterxml.jackson.databind.JsonNode;
import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.models.IA.AIModel;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Google Vertex AI — requer um token OAuth2 de acesso como Bearer e o nome da
 * localização (região) na URL base. O token é obtido fora desta classe (ex.:
 * via ADC / service account) e injetado por {@link #setKey(String)}.
 */
@Service
public class GoogleVertexAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.GOOGLE_VERTEX_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.GOOGLE_VERTEX_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://LOCATION-aiplatform.googleapis.com/v1";
    }

    @Override
    protected String modelsUrl() {
        // GET /v1beta1/publishers/{publisher}/models — a listagem precisa do
        // "parent" (publisher). O token OAuth2 entra como Bearer via setKey().
        return "https://LOCATION-aiplatform.googleapis.com/v1beta1/publishers/google/models";
    }

    @Override
    protected List<AIModel> parseModels(JsonNode root) {
        // Resposta do Vertex: {"publisherModels":[{"name":"publishers/google/models/gemini-2.0-flash", ...}]}
        // O id do modelo é o trecho final de "name" (após "models/").
        List<AIModel> models = new ArrayList<>();
        JsonNode items = root != null ? root.get("publisherModels") : null;
        if (items != null && items.isArray()) {
            items.forEach(node -> addPublisherModel(models, node));
        }
        return models;
    }

    private void addPublisherModel(List<AIModel> models, JsonNode node) {
        String fullName = textValue(node, "name");
        if (fullName == null || fullName.isBlank()) {
            return;
        }
        String id = fullName.substring(fullName.lastIndexOf('/') + 1);
        String displayName = textValue(node, "displayName");
        models.add(new AIModel(
                id,
                (displayName == null || displayName.isBlank()) ? id : displayName,
                getProvider(),
                defaultStreamSupport(),
                defaultToolsSupport(),
                defaultReasoningSupport()));
    }

    private static String textValue(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value != null && value.isValueNode() && !value.isNull()) ? value.asText() : null;
    }
}
