package com.quitto.server.domain.interfaces.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import java.util.List;

public interface AIProvider {

    void setKey(String secret);

    ServiceProvider getProvider();

    String getName();

    String getApiBaseURL();

    /**
     * Consulta a lista de modelos disponíveis no provedor.
     *
     * <p>Idealmente deve ser chamada somente quando {@link #isEnabled()} for
     * {@code true} e após {@link #setKey(String)} ter sido invocado. Lança
     * {@code ProviderException} se a chave estiver ausente ou a API falhar.</p>
     *
     * @return lista (possivelmente vazia) de modelos disponíveis
     */
    List<ModelInfo> getModels();

    boolean isEnabled();

    void turnOn();

    void turnOff();
}
