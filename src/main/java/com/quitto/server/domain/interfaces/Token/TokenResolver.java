package com.quitto.server.domain.interfaces.Token;

import java.util.Optional;

/**
 * Porta de domínio que abstrai a <strong>origem</strong> de um token de autenticação
 * (cookie, header {@code Authorization}, query param...). Leitura pura: cada
 * implementação retorna o token se a fonte contiver um, ou vazio. Novos resolvers
 * são beans Spring descobertos automaticamente (Open/Closed Principle).
 */
public interface TokenResolver {
    Optional<String> resolve(TokenRequestContext request);
}
