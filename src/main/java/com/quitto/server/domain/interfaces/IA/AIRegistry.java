package com.quitto.server.domain.interfaces.IA;

import java.util.List;
import java.util.Optional;


/**
 * Registrador do escopo IA (Ports &amp; Adapters).
 *
 * <p>Contrato genérico de um catálogo de itens do domínio de IA — provedores,
 * modelos etc. — indexados por uma chave tipada ({@code K}) e pesquisáveis
 * pelo nome. As implementações concretas vivem na infraestrutura (ex.:
 * {@code AIProviderRegistry}) e são beans Spring.</p>
 *
 * @param <K> tipo da chave de indexação (ex.: {@code ServiceProvider})
 * @param <V> tipo do item registrado (ex.: {@code AIProvider})
 * @param <M> tipo do modelo exposto por cada item (ex.: {@code AIModel})
 */
public interface AIRegistry<K, V, M> {

    Optional<List<V>> find(K key);

    Optional<List<V>> find(String name);

    Optional<V> findProvider(K provider);

    List<M> getModelsForProvaider(K provider);

    List<M> getAllModels();

    void register(K key, V value);

    List<V> getAll();

    int size();

    boolean containsKey(K key);

    boolean containsName(String name);
}
