package com.quitto.server.application.dto;

import com.quitto.server.domain.interfaces.OperationKey.OperationKey;

public interface BaseDTO {
    OperationKey idempotencyKey();
}
