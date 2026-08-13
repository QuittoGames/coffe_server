package com.quitto.server.application.dto.Auth;

import com.quitto.server.application.dto.BaseDTO;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;

public record LoginDTO(
        OperationKey idempotencyKey,
        String name,
        String password) implements BaseDTO {
}
