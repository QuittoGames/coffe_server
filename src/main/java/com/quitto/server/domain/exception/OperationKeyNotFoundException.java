package com.quitto.server.domain.exception;

/**
 * Exceção de domínio para chaves de operação com Key (valor) inválida.
 * Lançada quando um {@code OperationKey} não possui um valor válido
 * (ex.: valor nulo ou em branco) durante validação de idempotência.
 * Estende {@link RuntimeException} — tratamento opcional nas camadas superiores.
 */
public class OperationKeyNotFoundException extends RuntimeException {

    public OperationKeyNotFoundException(String message) {
        super(message);
    }
}
