package com.quitto.server.application.services.Indepotecy;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.exception.InvalidOperationKeyException;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.interfaces.OperationKey.OperationKeyManager;

@Service
public class IdepotecyService {

    private final OperationKeyManager operationKeyManager;

    public IdepotecyService(OperationKeyManager operationKeyManager) {
        this.operationKeyManager = operationKeyManager;
    }

    public void validateKey(OperationKey key) throws InvalidOperationKeyException {
        Objects.requireNonNull(key, "OperationKey cannot be null");

        if (!operationKeyManager.validated(key)
                || !operationKeyManager.validateStateKey(key)) {

            throw new InvalidOperationKeyException(
                    "The provided OperationKey is invalid or has an invalid state."
            );
        }
    }

    /**
     * Verifica se a chave já foi processada anteriormente (request duplicado).
     *
     * <p>Um cache hit no {@link OperationKeyManager} significa que a operação
     * associada a esta chave já foi concluída — o controller deve recusar o
     * request sem re-executar o efeito colateral.</p>
     */
    public boolean isDuplicate(OperationKey key) {
        Objects.requireNonNull(key, "OperationKey cannot be null");
        return operationKeyManager.validated(key);
    }

    /**
     * Marca a chave como processada, permitindo detectar requests duplicados
     * que cheguem depois (dentro da janela TTL da chave).
     */
    public void markProcessed(OperationKey key) {
        Objects.requireNonNull(key, "OperationKey cannot be null");
        operationKeyManager.register(key);
    }
}
