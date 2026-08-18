package com.quitto.server.domain.exception.Auth;

/**
 * Exceção de domínio para <strong>token inválido ou expirado</strong>.
 * Especialização de {@link AuthenticationException}: sinaliza que um token
 * (ex.: JWT) falhou na verificação — assinatura, issuer ou expiração.
 * Lançada pelo filtro de autenticação da infraestrutura.
 *
 * @see AuthenticationException
 */
public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
