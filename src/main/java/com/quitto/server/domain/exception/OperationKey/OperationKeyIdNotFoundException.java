package com.quitto.server.domain.exception.OperationKey;

/**
 * Exceção de domínio para chaves de operação com ID inválido.
 * Lançada quando um {@code OperationKey} não possui um ID válido
 * (ex.: ID nulo ou em branco) durante validação de idempotência.
 * Estende {@link RuntimeException} — tratamento opcional nas camadas superiores.
 */
public class OperationKeyIdNotFoundException extends RuntimeException {

    public OperationKeyIdNotFoundException(String message) {
        super(message);
    }
}
