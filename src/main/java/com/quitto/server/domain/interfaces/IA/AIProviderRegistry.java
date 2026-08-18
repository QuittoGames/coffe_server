package com.quitto.server.domain.interfaces.IA;

import java.util.List;
import java.util.Optional;

import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.exception.IA.ProviderNotFoundException;

/**
 * Porta de domínio do registro de provedores de IA.
 *
 * <p>Substitui o registro genérico legado ({@code AIRegistry<K,V,M>}) por um
 * contrato específico do ecossistema IA: indexa {@link AIProvider} pela chave
 * {@link AIProviderType}. As implementações concretas vivem na infraestrutura
 * (ex.: {@code DefaultAIProviderRegistry}) e são beans Spring.</p>
 */
public interface AIProviderRegistry {

    /** Todos os provedores registrados. */
    List<AIProvider> getAll();

    /** Provedor registrado para o tipo, se existir. */
    Optional<AIProvider> find(AIProviderType type);

    /**
     * Provedor registrado para o tipo, ou {@link ProviderNotFoundException}
     * se não existir.
     */
    AIProvider findOrThrow(AIProviderType type);

    /** {@code true} se existe um provedor registrado para o tipo. */
    boolean contains(AIProviderType type);

    /** Quantidade de provedores registrados. */
    int size();
}