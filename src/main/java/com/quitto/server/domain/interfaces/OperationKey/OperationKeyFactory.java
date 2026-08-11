package com.quitto.server.domain.interfaces.OperationKey;

import java.time.Duration;

/**
 * Porta de fábrica de chaves operacionais (domínio puro).
 *
 * <p>Abstração geral para criação de chaves de operação de uso direto do
 * usuário — ex.: {@code IdempotencyKey} para idempotência. Não é válida
 * para tokens de autenticação (JWT etc.).</p>
 */
public interface OperationKeyFactory {

    OperationKey create();

    OperationKey create(Duration ttl);

}
