package com.quitto.server.domain.exception.Auth;

/**
 * Exceção de domínio para senhas que não atendem às regras de validação.
 * Lançada quando uma senha viola a política de segurança do sistema
 * (ex.: tamanho mínimo de 8 caracteres, validado em {@code User.changePassword}).
 * Estende {@link RuntimeException} — tratamento opcional nas camadas superiores.
 */
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
