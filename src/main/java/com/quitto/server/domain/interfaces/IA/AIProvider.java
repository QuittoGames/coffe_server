package com.quitto.server.domain.interfaces.IA;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.models.IA.AIModel;

import java.util.List;

/**
 * Porta de domínio para um <strong>provedor de IA</strong> (OpenAI, Anthropic,
 * Ollama, etc.).
 *
 * <p>Define o contrato de identidade, ciclo de vida, chave de acesso e
 * catálogo de modelos de um provedor. A implementação abstrata de referência é
 * o {@code BaseProvider} (na infraestrutura), e cada provedor concreto é um
 * adapter que herda dele e configura apenas identidade e URL base.</p>
 *
 * <p>A relação <strong>1:N</strong> (um provedor → vários {@link AIModel}) é o
 * coração do registro de IA: {@link #getModels()} expõe o catálogo que o
 * {@code AIRegistry} agrega por chave ({@link ServiceProvider}).</p>
 *
 * @see com.quitto.server.domain.enums.ServiceProvider
 * @see com.quitto.server.domain.models.IA.AIModel
 */
public interface AIProvider {

    void setKey(String secret);

    ServiceProvider getProvider();

    String getName();

    String getApiBaseURL();

    List<AIModel> getModels();

    void fetchModelsFromApi();

    boolean isEnabled();

    void turnOn();

    void turnOff();
}
