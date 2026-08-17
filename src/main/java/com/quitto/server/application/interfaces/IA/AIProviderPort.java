package com.quitto.server.application.interfaces.IA;

import java.util.List;

import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.models.IA.AIModel;

/**
 * Porta de aplicação para consulta ao ecossistema de provedores de IA.
 *
 * <p>Expõe operações de alto nível (catálogo de providers, catálogo de modelos,
 * busca por id) para os controllers REST. A implementação de referência é o
 * use case {@code ProviderService} (application/services/IA).</p>
 */
public interface AIProviderPort {

    /** Todos os provedores registrados. */
    List<AIProvider> getAllProviders();

    /** Todos os modelos de todos os provedores registrados (achatado). */
    List<AIModel> getAllModels();

    /**
     * Busca um modelo pelo id em todos os provedores.
     *
     * @throws IllegalArgumentException se nenhum modelo com o id existir
     */
    AIModel getModel(String modelId);

    /**
     * Encontra um provedor pelo nome (string, case-insensitive).
     *
     * @throws IllegalArgumentException se o nome não for um {@code AIProviderType} válido
     */
    AIProvider findProvider(String providerString);
}