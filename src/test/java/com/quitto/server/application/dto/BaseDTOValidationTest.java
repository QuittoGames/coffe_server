package com.quitto.server.application.dto;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.valueobject.IdempotencyKey.IdempotencyKey;

class BaseDTOValidationTest {

    private OperationKey key() {
        LocalDateTime now = LocalDateTime.now();
        return new IdempotencyKey(
                UUID.randomUUID(),
                UUID.randomUUID(),
                now,
                now.plusHours(1));
    }

    @Test
    void loginDtoImplementsBaseDTO() {
        LoginDTO withKey = new LoginDTO(key(), "quitto", "senha");
        LoginDTO withoutKey = new LoginDTO(null, "quitto", "senha");

        assertTrue(withKey instanceof BaseDTO);
        assertTrue(withoutKey instanceof BaseDTO);
        assertTrue(withKey.idempotencyKey() != null);
        assertNull(withoutKey.idempotencyKey());
    }

    @Test
    void registerDtoImplementsBaseDTO() {
        RegisterDTO withKey = new RegisterDTO(key(), "quitto", "senha", "quitto@ex.com");
        RegisterDTO withoutKey = new RegisterDTO(null, "quitto", "senha", "quitto@ex.com");

        assertTrue(withKey instanceof BaseDTO);
        assertTrue(withoutKey instanceof BaseDTO);
        assertTrue(withKey.idempotencyKey() != null);
        assertNull(withoutKey.idempotencyKey());
    }

    @Test
    void errorResponseImplementsBaseDTO() {
        ErrorResponse withKey = new ErrorResponse(key(), "msg");
        ErrorResponse withoutKey = new ErrorResponse(null, "msg");

        assertTrue(withKey instanceof BaseDTO);
        assertTrue(withoutKey instanceof BaseDTO);
        assertTrue(withKey.idempotencyKey() != null);
        assertNull(withoutKey.idempotencyKey());
    }
}