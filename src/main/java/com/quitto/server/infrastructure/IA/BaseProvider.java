package com.quitto.server.infrastructure.IA;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.models.IA.AIModel;

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
 * "lista de modelos" em {@link AIModel} do domínio, expondo ganchos (hooks)
 * que cada provedor concreto pode sobrescrever para a própria forma de
 * autenticação, formato de resposta e capacidades padrão do catálogo.</p>
 *
 * <p>Cada provedor possui a sua própria lista de modelos ({@code models:
 * List&lt;AIModel&gt;} na arquitetura / drawio). {@link #fetchModelsFromApi()}
 * é a função própria que sabe <em>como</em> carregar o catálogo direto da API
 * do provedor; {@link #getModels()} faz a leitura cacheada, populando o cache
 * na primeira consulta.</p>
 *
 * <p>Regras de arquitetura: esta classe é pura infraestrutura — o domínio
 * ({@code AIProvider}, {@code AIModel}, {@code ServiceProvider}) não é
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
    protected String envId; // ID used for env var lookup (e.g., "OPENAI")
    private boolean enabled;

    /**
     * Lista de modelos do provedor (drawio: {@code models: List<AIModel>}).
     * Preenchida na primeira chamada a {@link #getModels()} e cacheada.
     */
    protected List<AIModel> models = List.of();

    // ─── Hooks de configuração (sobrescreva por provedor se necessário) ───

    /** {@code true} (padrão) envia a chave no header {@code Authorization: Bearer}. */
    protected boolean usesBearer() {
        return true;
    }

    /** {@code true} (padrão) exige que {@code setKey} tenha sido chamado antes de {@code getModels}. */
    @Override
    public boolean requiresKey() {
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

    // ─── Hooks de capacidades do catálogo (drawio: AIModel stream/tools/reasoning) ───

    /**
     * Capacidade padrão de streaming dos modelos do provedor.
     * Sobrescreva por provedor quando o catálogo divergir.
     */
    protected boolean defaultStreamSupport() {
        return true;
    }

    /**
     * Capacidade padrão de ferramentas (function calling) dos modelos do provedor.
     * Sobrescreva por provedor quando o catálogo divergir.
     */
    protected boolean defaultToolsSupport() {
        return true;
    }

    /**
     * Capacidade padrão de raciocínio dos modelos do provedor.
     * Sobrescreva por provedor quando o catálogo divergir.
     */
    protected boolean defaultReasoningSupport() {
        return false;
    }

    // ─── AIProvider (coração) ───

    @Override
    public void setKey(String secret) {
        this.apiKey = secret;
    }

    /**
     * Define o ID usado para busca de variáveis de ambiente (ex.: {@code "OPENAI"}).
     * Chamado pelo initializer de cada provedor concreto ({@code { setEnvId("OPENAI"); }}).
     *
     * @param envId ID de busca de ambiente; {@code null} faz o registry cair no
     *              nome do enum {@code ServiceProvider}
     */
    protected void setEnvId(String envId) {
        this.envId = envId;
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

    /**
     * Leitura cacheada do catálogo de modelos (drawio:
     * {@code models: List<AIModel>}). Popula o cache na primeira chamada
     * delegando a {@link #fetchModelsFromApi()} e reutiliza o valor nas
     * chamadas seguintes.
     */
    @Override
    public List<AIModel> getModels() {
        if (models.isEmpty()) {
            fetchModelsFromApi();
        }
        return models;
    }

    /**
     * Busca a lista de modelos na API do provedor e armazena no cache usado
     * por {@link #getModels()} (drawio: {@code getProvaidersModels(): void}),
     * convertendo o JSON de resposta em {@link AIModel} do domínio.
     */
    @Override
    public void fetchModelsFromApi() {
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
            this.models = parseModels(root); // preenche o cache usado por getModels()
        } catch (ProviderException e) {
            throw e;
        } catch (Exception e) {
            String detail = (e.getMessage() == null || e.getMessage().isBlank())
                    ? e.getClass().getSimpleName()
                    : e.getMessage();
            throw new ProviderException("Falha ao listar modelos do provedor '"
                    + getName() + "': " + detail, e);
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
     * Converte o JSON resposta em uma lista de {@link AIModel}, tolerando os
     * formatos mais comuns da indústria:
     * <ul>
     *   <li>{@code {"data":[{id, created, owned_by}...]}} (OpenAI-compatible)</li>
     *   <li>{@code {"models":[{name, created, owner}...]}} (Google, Cohere, Ollama)</li>
     *   <li>array raiz {@code [{id|name...}]} (Together, Hugging Face)</li>
     *   <li>{@code {"result":[...]}} (Cloudflare)</li>
     *   <li>{@code {"resources":[...]}} (IBM watsonx)</li>
     *   <li>{@code {"results":[...]}} (Replicate)</li>
     * </ul>
     *
     * <p>De cada nó são extraídos o {@code id} (com fallback em
     * {@code name}/{@code model_id}/{@code model_name}) como
     * {@code providerModelId} e o {@code name} amigável. As capacidades
     * (stream/tools/reasoning) são decididas pelos hooks do provedor, já que
     * não são expostas pela API de listagem.</p>
     */
    protected List<AIModel> parseModels(JsonNode root) {
        List<AIModel> models = new ArrayList<>();
        for (String field : new String[]{"data", "models", "result", "resources", "results"}) {
            JsonNode items = firstNode(root, field);
            if (items != null && items.isArray()) {
                items.forEach(node -> addModel(models, node));
                return models;
            }
        }
        if (root.isArray()) {
            root.forEach(node -> addModel(models, node));
        }
        return models;
    }

    /**
     * Converte um nó do JSON de listagem em um {@link AIModel} e adiciona à
     * lista. Nós sem identidade (id/name/model_id/model_name) são ignorados.
     */
    private void addModel(List<AIModel> models, JsonNode node) {
        String providerModelId = firstText(node, "id", "name", "model_id", "model_name");
        if (providerModelId == null) {
            return;
        }
        String name = firstText(node, "name", "id");
        models.add(new AIModel(
                providerModelId,
                name == null ? providerModelId : name,
                getProvider(),
                defaultStreamSupport(),
                defaultToolsSupport(),
                defaultReasoningSupport()));
    }

    // ─── Utilitários ───

    protected static JsonNode firstNode(JsonNode root, String field) {
        return root != null ? root.get(field) : null;
    }

    /** Retorna o primeiro campo não-vazio entre os nomes informados, ou {@code null}. */
    private static String firstText(JsonNode node, String... fields) {
        for (String field : fields) {
            String value = text(node, field);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
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

    // Getters para a interface AIProvider
    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getEnvId() {
        return envId;
    }

    // auxiliares para provedores que queiram sobrescrever fetchModelsFromApi()
    // (ou getModels por completo) e precisem do HttpClient/ObjectMapper
    protected static HttpClient http() {
        return HTTP;
    }

    protected static ObjectMapper mapper() {
        return MAPPER;
    }
}
