package com.quitto.server.domain.exception;

/**
 * Exceção de domínio base para falhas de autenticação.
 *
 * <p>Raiz da hierarquia de erros de autenticação do sistema. Usada como
 * contrato pela porta {@code AuthenticationService} (domínio) — a
 * infraestrutura traduz exceções do Spring Security para esta exceção antes
 * de propagá-las. Base para exceções mais específicas como
 * {@link InvalidTokenException}. Garante que nenhuma exceção de framework
 * vaze para as camadas internas.</p>
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
