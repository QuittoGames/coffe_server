package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

/**
 * Hugging Face Hub — a listagem de modelos é pública, então não exige chave.
 * Uma chave (Bearer token) é opcional para modelos gated.
 */
@Service
public class HuggingFaceProvider extends BaseProvider {

    {
        setEnvId("HUGGING_FACE");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.HUGGING_FACE;
    }

    @Override
    public String getName() {
        return ServiceProvider.HUGGING_FACE.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://huggingface.co/api";
    }

    @Override
    public boolean requiresKey() {
        return false;
    }
}
