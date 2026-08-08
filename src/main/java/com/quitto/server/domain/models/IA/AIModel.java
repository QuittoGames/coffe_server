package com.quitto.server.domain.models.IA;

import java.util.Objects;

import com.quitto.server.domain.enums.ServiceProvider;

/**
 * Representa um modelo de IA oferecido por um provedor.
 *
 * <p>É a unidade do catálogo de IA: cada instância identifica um modelo
 * concreto (ex.: {@code gpt-4o} da OpenAI) pelo {@code providerModelId} e
 * descreve suas capacidades (streaming, ferramentas, raciocínio). A relação
 * é 1:N — um provedor ({@link ServiceProvider}) expõe vários modelos via
 * {@code AIProvider.getModels()}.</p>
 *
 * <p><strong>Imutabilidade:</strong> a classe é {@code final} e todos os
 * campos são {@code final} — uma vez criado, um modelo não muda. Campos
 * obrigatórios ({@code providerModelId}, {@code name}, {@code provider}) são
 * validados com {@link Objects#requireNonNull} no construtor.</p>
 */
public final class AIModel {

    private final String providerModelId;
    private final String name;
    private final ServiceProvider provider;

    private final boolean stream;

    private final boolean tools;

    private final boolean reasoning;

    public AIModel(
            String providerModelId,
            String name,
            ServiceProvider provider,
            boolean stream,
            boolean tools,
            boolean reasoning) {

        this.name = Objects.requireNonNull(name);
        this.provider = Objects.requireNonNull(provider);
        this.providerModelId = Objects.requireNonNull(providerModelId);

        this.stream = stream;
        this.tools = tools;
        this.reasoning = reasoning;
    }

    public String getId() {
        return providerModelId;
    }

    public ServiceProvider getProvider() {
        return provider;
    }

    public String getName(){
        return name;
    }

    public boolean supportsStreaming() {
        return stream;
    }

    public boolean supportsTools() {
        return tools;
    }

    public boolean supportsReasoning() {
        return reasoning;
    }
}
