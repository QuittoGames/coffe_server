package com.quitto.server.domain.exception;

/**
 * Exceção de domínio para <strong>usuário não encontrado</strong>.
 * Espelho de {@link MachineNotFoundException}: sinaliza que um usuário não
 * existe em operações de repositório/serviço. Candidata a substituir a
 * {@code UsernameNotFoundException} (Spring) que hoje vaza da aplicação.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super("User not found with id: " + userId);
    }
}
