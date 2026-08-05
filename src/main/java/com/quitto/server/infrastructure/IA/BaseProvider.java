package com.quitto.server.infrastructure.IA;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.IA.ModelInfo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação abstrata comum a todos os provedores de IA.
 *
 * <p>Centraliza o uso de {@code HttpClient} + Jackson e o parse do JSON de
 * "lista de modelos", expondo ganchos (hooks) que cada provedor concreto
 * pode sobrescrever para a própria forma de autenticação e formato de
 * resposta.</p>
 *
 * <p>Regras de arquitetura: esta classe é pura infraestrutura — o domínio
 * ({@code AIProvider}, {@code ModelInfo}, {@code ServiceProvider}) não é
 * contaminado com HTTP/Jackson.</p>
 */
public abstract class BaseProvider implements AIProvider {

    protected static final String AUTHORIZATION_BEARER = "Authorization: Bearer ";

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    protected String apiKey;
    private boolean enabled;

    // ─── Hooks de configuração (sobrescreva por provedor se necessário) ───

    /** {@code true} (padrão) envia a chave no header {@code Authorization: Bearer}. */
    protected boolean usesBearer() {
        return true;
    }

    /** {@code true} (padrão) exige que {@code setKey} tenha sido chamado antes de {@code getModels}. */
    protected boolean requiresKey() {
        return true;
    }

    /** Nome do header customizado para provedores que não usam Bearer (ex.: x-api-key). */
    protected Optional<String> apiKeyHeaderName() {
        return Optional.empty();
    }

    /** Nome do query param para provedores que autenticam por query string (ex.: Gemini {@code key}). */
    protected Optional<String> apiKeyQueryParamName() {
        return Optional.empty();
    }

    /**
     * Headers adicionais fixos exigidos por alguns provedores (ex.: Anthropic
     * {@code anthropic-version}). Padrão: vazio.
     */
    protected java.util.Map<String, String> extraHeaders() {
        return java.util.Map.of();
    }

    /**
     * URL completa usada para listar modelos. Padrão: {@code <baseUrl>/models}.
     */
    protected String modelsUrl() {
        String base = getApiBaseURL();
        if (base == null || base.isBlank()) {
            throw new ProviderException("URL base da API não configurada para " + getName());
        }
        String url = stripTrailingSlash(base) + "/models";
        if (apiKeyQueryParamName().isPresent() && apiKey != null && !apiKey.isBlank()) {
            url += "?" + apiKeyQueryParamName().get() + "=" + encode(apiKey);
        }
        return url;
    }

    // ─── AIProvider (coração) ───

    @Override
    public void setKey(String secret) {
        this.apiKey = secret;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void turnOn() {
        this.enabled = true;
    }

    @Override
    public void turnOff() {
        this.enabled = false;
    }

    @Override
    public List<ModelInfo> getModels() {
        if (requiresKey() && (apiKey == null || apiKey.isBlank())) {
            throw new ProviderException(
                    "Chave da API não configurada para o provedor '" + getName()
                            + "'. Chame setKey(...) antes de getModels().");
        }

        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(modelsUrl()))
                    .GET()
                    .timeout(Duration.ofSeconds(30))
                    .header("Accept", "application/json");

            applyAuth(builder);
            applyExtraHeaders(builder);

            HttpResponse<String> response = HTTP.send(builder.build(),
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ProviderException(
                        "Provedor '" + getName() + "' respondeu com status "
                                + response.statusCode() + ": " + truncate(response.body()));
            }

            JsonNode root = MAPPER.readTree(response.body());
            return parseModels(root);
        } catch (ProviderException e) {
            throw e;
        } catch (Exception e) {
            throw new ProviderException("Falha ao listar modelos do provedor '"
                    + getName() + "': " + e.getMessage(), e);
        }
    }

    /**
     * Aplica o esquema de autenticação escolhido aos hooks.
     */
    private void applyAuth(HttpRequest.Builder builder) {
        if (apiKey == null || apiKey.isBlank()) {
            return; // exigência já validada em getModels
        }
        Optional<String> headerName = apiKeyHeaderName();
        if (headerName.isPresent()) {
            builder.header(headerName.get(), apiKey);
            return;
        }
        if (usesBearer()) {
            builder.header("Authorization", "Bearer " + apiKey);
        }
    }

    private void applyExtraHeaders(HttpRequest.Builder builder) {
        extraHeaders().forEach(builder::header);
    }

    /**
     * Converte o JSON resposta em uma lista de modelos, tolerando os formatos
     * mais comuns da indústria:
     * <ul>
     *   <li>{@code {"data":[{id, created, owned_by}...]}} (OpenAI-compatible)</li>
     *   <li>{@code {"models":[{name, created, owner}...]}} (Google, Cohere, Ollama)</li>
     *   <li>array raiz {@code [{id|name...}]} (Together, Hugging Face)</li>
     *   <li>{@code {"result":[...]}} (Cloudflare)</li>
     *   <li>{@code {"resources":[...]}} (IBM watsonx)</li>
     *   <li>{@code {"results":[...]}} (Replicate)</li>
     * </ul>
     */
    protected List<ModelInfo> parseModels(JsonNode root) {
        List<ModelInfo> models = new ArrayList<>();
        for (String field : new String[]{"data", "models", "result", "resources", "results"}) {
            JsonNode items = firstNode(root, field);
            if (items != null && items.isArray()) {
                items.forEach(node -> models.add(nodeToModel(node)));
                return models;
            }
        }
        if (root.isArray()) {
            root.forEach(node -> models.add(nodeToModel(node)));
        }
        return models;
    }

    private ModelInfo nodeToModel(JsonNode node) {
        String id = text(node, "id");
        if (id == null) id = text(node, "name");
        if (id == null) id = text(node, "model_id");
        if (id == null) id = text(node, "model_name");

        Long created = null;
        if (node.hasNonNull("created") && node.get("created").canConvertToLong()) {
            created = node.get("created").asLong();
        }

        String ownedBy = text(node, "owned_by");
        if (ownedBy == null) ownedBy = text(node, "owner");
        if (ownedBy == null) ownedBy = text(node, "organization");

        return new ModelInfo(id == null ? "" : id, created, ownedBy);
    }

    // ─── Utilitários ───

    protected static JsonNode firstNode(JsonNode root, String field) {
        return root != null ? root.get(field) : null;
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value != null && value.isValueNode() && !value.isNull()) ? value.asText() : null;
    }

    private static String stripTrailingSlash(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    private static String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value;
        }
    }

    private static String truncate(String body) {
        if (body == null) return "";
        return body.length() > 200 ? body.substring(0, 200) + "…" : body;
    }

    // auxiliar para provedores (Contexto) que queiram sobrescrever getModels por completo
    protected static HttpClient http() {
        return HTTP;
    }

    protected static ObjectMapper mapper() {
        return MAPPER;
    }
}