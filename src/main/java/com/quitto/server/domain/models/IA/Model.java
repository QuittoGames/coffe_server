package com.quitto.server.domain.models.IA;

import java.util.Objects;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.interfaces.IA.ModelInfo;

public final class Model {

    /**
     * Informações públicas do modelo retornadas pelo provedor.
     */
    private final ModelInfo info;

    /**
     * Provedor responsável por este modelo.
     */
    private final ServiceProvider provider;

    /**
     * ID utilizado pelo provedor.
     * Ex.: "deepseek-ai/deepseek-v4-pro"
     */
    private final String providerModelId;

    /**
     * Recursos suportados.
     */
    private final boolean stream;

    private final boolean tools;

    private final boolean reasoning;

    public Model(
            ModelInfo info,
            ServiceProvider provider,
            String providerModelId,
            boolean stream,
            boolean tools,
            boolean reasoning) {

        this.info = Objects.requireNonNull(info);
        this.provider = Objects.requireNonNull(provider);
        this.providerModelId = Objects.requireNonNull(providerModelId);

        this.stream = stream;
        this.tools = tools;
        this.reasoning = reasoning;
    }

    public ModelInfo getInfo() {
        return info;
    }

    public String getId() {
        return info.id();
    }

    public ServiceProvider getProvider() {
        return provider;
    }

    public String getProviderModelId() {
        return providerModelId;
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
