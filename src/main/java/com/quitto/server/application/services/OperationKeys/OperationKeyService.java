package com.quitto.server.application.services.OperationKeys;

import java.security.InvalidKeyException;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.interfaces.OperationKey.OperationKeyFactory;
import com.quitto.server.infrastructure.services.Indepotecy.IdepotencyManager;

@Service
public class OperationKeyService {

    private final IdepotencyManager idempotencyManager;
    private final OperationKeyFactory operationKeyFactory;

    public OperationKeyService(IdepotencyManager idempotencyManager, OperationKeyFactory operationKeyFactory) {
        this.idempotencyManager = idempotencyManager;
        this.operationKeyFactory = operationKeyFactory;
    }

    public void execute() throws InvalidKeyException {

        OperationKey key = operationKeyFactory.create();

        if (!idempotencyManager.validadatedIndepotetion(key)) {
            idempotencyManager.register(key);

            // executa operação
        }
    }
}
