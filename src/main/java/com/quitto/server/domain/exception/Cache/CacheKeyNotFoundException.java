package com.quitto.server.domain.exception.Cache;

/**
 * Exceção de domínio para chaves não encontradas no cache.
 *
 * <p>Lançada pelo {@code CacheService} quando uma operação exige o valor
 * armazenado em uma chave (uso obrigatório), mas a chave não existe no cache
 * (cache miss). Diferente de {@code Optional.empty()}, esta exceção sinaliza
 * que a ausência do valor é um erro para o fluxo que a invocou.</p>
 *
 * <p>Estende {@link RuntimeException} — tratamento opcional nas camadas
 * superiores. Nenhuma dependência de framework: pertence ao domínio puro.</p>
 */
public class CacheKeyNotFoundException extends RuntimeException {

    public CacheKeyNotFoundException(String message) {
        super(message);
    }
}