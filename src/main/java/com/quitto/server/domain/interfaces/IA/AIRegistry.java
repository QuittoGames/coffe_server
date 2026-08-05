package com.quitto.server.domain.interfaces.IA;

import java.util.List;
import java.util.Optional;

/**
 * Registrador do escopo IA (Ports &amp; Adapters).
 *
 * <p>Contrato genérico de um catálogo de itens do domínio de IA — provedores,
 * modelos etc. — indexados por uma chave tipada ({@code K}) e pesquisáveis
 * pelo nome. As implementações concretas vivem na infraestrutura (ex.:
 * {@code AIProviderRegistry}, {@code ModelsRegistry}) e são beans Spring.</p>
 *
 * @param <K> tipo da chave de indexação
 * @param <V> tipo do item registrado
 */
public interface AIRegistry<K, V> {

    /**
     * Busca um item pela chave de indexação.
     *
     * @param key chave de indexação do item
     * @return o item registrado, ou vazio se não existir
     */
    Optional<List<V>> find(K key);

    /**
     * Busca um item pelo nome, sem diferenciar maiúsculas/minúsculas.
     *
     * @param name nome do item
     * @return o item registrado, ou vazio se não existir
     */
    Optional<List<V>> find(String name);

    /**
     * Registra (ou substitui) o item na chave informada.
     *
     * @param key   chave de indexação do item
     * @param value item a ser registrado
     */
    void register(K key, V value);

    /**
     * Retorna todos os itens registrados.
     *
     * @return lista (imutável) dos itens registrados
     */
    List<V> getAll();

    /**
     * Quantidade de itens registrados.
     *
     * @return número de itens no catálogo
     */
    int size();

    /**
     * Verifica se existe item para a chave informada.
     *
     * @param key chave de indexação a verificar
     * @return {@code true} se a chave está registrada
     */
    boolean containsKey(K key);

    /**
     * Verifica se existe item com o nome informado.
     *
     * @param name nome a verificar
     * @return {@code true} se o nome está registrado
     */
    boolean containsName(String name);
}
