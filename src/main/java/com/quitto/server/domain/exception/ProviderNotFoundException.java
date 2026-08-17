package com.quitto.server.domain.exception;

/**
 * Exceção de domínio para provedor de IA não encontrado no registry.
 *
 * <p>Lançada por {@code AIProviderRegistry.findOrThrow(...)} quando nenhum
 * provedor está registrado para o {@code AIProviderType} solicitado.</p>
 */
public class ProviderNotFoundException extends ProviderException {

    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}