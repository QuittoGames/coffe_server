package com.quitto.server.domain.enums.cache;

/**
 * Namespaces de chaves usados no cache Redis (instância {@code "cache"}).
 *
 * <p>Cada constante define o prefixo de chave de um tipo de dado cacheado
 * (tokens de autenticação, sessões de usuário e catálogo de modelos de IA).
 * O método {@link #key(String)} monta a chave completa no formato
 * {@code <prefixo>:<id>}. Centraliza os prefixos, evitando colisões de chave
 * e erros de digitação espalhados pelo código.</p>
 */
public enum CacheNamespace {

    USER_TOKEN("cache:auth:token"),
    USER_SESSION("cache:session"),
    MODELS("cache:models");

    private final String prefix;

    CacheNamespace(String prefix) {
        this.prefix = prefix;
    }

    public String key(String id) {
        return prefix + ":" + id;
    }
}
