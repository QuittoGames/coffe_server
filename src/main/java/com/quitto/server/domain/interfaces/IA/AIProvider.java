package com.quitto.server.domain.interfaces.IA;

import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.models.IA.AIModel;

import java.util.List;

/**
 * Porta de domínio para um <strong>provedor de IA</strong> (OpenAI, OpenRouter,
 * Gemini, etc.).
 *
 * <p>Define o contrato de identidade, ciclo de vida, chave de acesso e
 * catálogo de modelos de um provedor. A implementação abstrata de referência é
 * o {@code BaseProvider} (na infraestrutura), e cada provedor concreto é um
 * adapter que herda dele e configura apenas identidade e URL base.</p>
 *
 * <p>A relação <strong>1:N</strong> (um provedor → vários {@link AIModel}) é o
 * coração do registro de IA: {@link #getModels()} expõe o catálogo que o
 * {@link AIProviderRegistry} agrega por chave ({@link AIProviderType}).</p>
 *
 * @see com.quitto.server.domain.enums.IA.AIProviderType
 * @see com.quitto.server.domain.models.IA.AIModel
 * @see com.quitto.server.domain.interfaces.IA.AIProviderRegistry
 */
public interface AIProvider {

    void setKey(String secret);

    AIProviderType getProvider();

    String getName();

    String getApiBaseURL();

    List<AIModel> getModels();

    void fetchModelsFromApi();

    boolean isEnabled();

    void turnOn();

    void turnOff();

    /**
     * Indica se o provedor está pronto para uso: configurado com a chave
     * necessária (quando {@link #requiresKey()} é {@code true}) e habilitado.
     *
     * @return {@code true} se o provedor pode listar/executar modelos
     */
    boolean isConfigured();

    /**
     * Retorna o ID usado para busca de variáveis de ambiente (ex: "OPENAI").
     * Este ID é usado pelo {@code CoffeAgentService} para buscar a chave
     * de API apropriada nas variáveis de ambiente.
     *
     * @return o ID de busca de ambiente, ou {@code null} se não configurado
     */
    default String getEnvId() {
        return null;
    }

    /**
     * Indica se o provedor exige uma chave de API para listar/executar modelos.
     * <p>Provedores self-hosted (Ollama, VLLM, etc.) retornam {@code false} —
     * o registry usa este contrato para desabilitar provedores que exigem chave
     * e não têm chave configurada (D3: o catálogo não falha inteiro).</p>
     *
     * @return {@code true} se o provedor precisa de chave de API
     */
    default boolean requiresKey() {
        return true;
    }
}