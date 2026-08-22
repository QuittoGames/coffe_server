package com.quitto.server.domain.interfaces.Token;

import java.util.Optional;

/**
 * Porta de domínio para o serviço de tokens (ex.: JWT) — geração, verificação
 * e extração do subject. {@code ID} é o tipo do identificador, restrito a
 * subtipos de {@link Number} (ex.: {@code Long}, {@code Integer}).
 */
public interface TokenService<ID extends Number>{
    String generateToken(Long id);

    boolean verifyToken(String token);

    Optional<ID> extractIdSubject(String token);

    Optional<String> reafresh(Long id);
}
