package com.quitto.server.infrastructure.IA;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.models.IA.AIModel;

/**
 * Base abstrata dos provedores de IA (camada de infraestrutura).
 *
 * <p>Centraliza o estado comum a todo provedor: chave de API
 * ({@code apiKey}), ID de busca de ambiente ({@code envId}), estado
 * (habilitado/desabilitado) e o catálogo de {@link AIModel}s. Cada provedor
 * concreto herda desta classe e configura apenas identidade
 * ({@link #getProvider()}, {@link #getName()}) e URL base
 * ({@link #getApiBaseURL()}).</p>
 *
 * <p><strong>Estado atual:</strong> a listagem real de modelos
 * ({@link #fetchModelsFromApi()}) ainda não está implementada — a chamada
 * lança {@link UnsupportedOperationException} e {@link #getModels()} retorna
 * o catálogo vazio. A integração HTTP por provedor é trabalho futuro
 * (endpoints de listagem, autenticação, parsing).</p>
 *
 * <p>Regra de camada: esta classe pertence à infraestrutura e pode usar
 * frameworks; o domínio ({@code AIProvider}) permanece 100% puro.</p>
 */
public abstract class BaseProvider implements AIProvider {

    protected static final Logger log = LoggerFactory.getLogger(BaseProvider.class);

    private String apiKey;
    private String envId;
    private boolean enabled = true;
    private final List<AIModel> models;

    protected BaseProvider() {
        this.models = List.of();
    }

    @Override
    public void setKey(String secret) {
        this.apiKey = secret;
    }

    @Override
    public List<AIModel> getModels() {
        return models;
    }

    @Override
    public void fetchModelsFromApi() {
        throw new UnsupportedOperationException(
                "fetchModelsFromApi() not implemented yet for provider: " + getName());
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
    public boolean isConfigured() {
        return requiresKey() ? apiKey != null && !apiKey.isBlank() : true;
    }

    @Override
    public String getEnvId() {
        return envId;
    }

    /**
     * Define o ID usado para busca de variáveis de ambiente (ex: "OPENAI").
     * Chamado no construtor de cada provedor concreto.
     */
    protected void setEnvId(String envId) {
        this.envId = envId;
    }
}
