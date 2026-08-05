package com.quitto.server.domain.exception;

/**
 * Exceção de domínio para falhas na interação com provedores de IA.
 *
 * <p>Usada para sinalizar problemas de configuração (chave ausente), erros de
 * rede/HTTP ao consultar a API do provedor, ou autenticação rejeitada.</p>
 */
public class ProviderException extends RuntimeException {

    public ProviderException(String message) {
        super(message);
    }

    public ProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}