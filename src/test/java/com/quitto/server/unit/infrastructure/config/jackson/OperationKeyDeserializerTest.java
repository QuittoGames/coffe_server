package com.quitto.server.unit.infrastructure.config.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.infrastructure.config.jackson.OperationKeyDeserializer;

class OperationKeyDeserializerTest {

    private JsonMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = JsonMapper.builder()
                .addModule(new SimpleModule()
                        .addDeserializer(OperationKey.class, new OperationKeyDeserializer()))
                .build();
    }

    @Test
    void deserializesFullKey() throws JacksonException {
        OperationKey key = mapper.readValue("""
                {"id":"11111111-1111-1111-1111-111111111111",
                 "value":"22222222-2222-2222-2222-222222222222",
                 "createdAt":"2026-08-13T16:00:00",
                 "expiresAt":"2026-08-13T16:10:00"}""", OperationKey.class);

        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), key.getId());
        assertEquals(UUID.fromString("22222222-2222-2222-2222-222222222222"), key.getValue());
        assertEquals(LocalDateTime.parse("2026-08-13T16:00:00"), key.getCreationDate());
        assertEquals(Instant.parse("2026-08-13T16:10:00Z"), key.expiresAt());
    }

    @Test
    void deserializesMinimalKeyWithDefaults() throws JacksonException {
        OperationKey key = mapper.readValue("""
                {"id":"11111111-1111-1111-1111-111111111111",
                 "value":"22222222-2222-2222-2222-222222222222"}""", OperationKey.class);

        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), key.getId());
        assertEquals(UUID.fromString("22222222-2222-2222-2222-222222222222"), key.getValue());
        assertNotNull(key.getCreationDate());
        assertNotNull(key.expiresAt());
        assertEquals(Duration.ofMinutes(10), Duration.between(key.createdAt(), key.expiresAt()));
    }

    @Test
    void createdAtWithOffsetParsesAsInstant() throws JacksonException {
        OperationKey key = mapper.readValue("""
                {"id":"11111111-1111-1111-1111-111111111111",
                 "value":"22222222-2222-2222-2222-222222222222",
                 "createdAt":"2026-08-13T16:00:00Z"}""", OperationKey.class);

        assertEquals(Instant.parse("2026-08-13T16:00:00Z"), key.createdAt());
    }

    @Test
    void missingId_throwsMismatchedInput() {
        assertThrows(MismatchedInputException.class, () -> mapper.readValue("""
                {"value":"22222222-2222-2222-2222-222222222222"}""", OperationKey.class));
    }

    @Test
    void invalidUuid_throwsMismatchedInput() {
        assertThrows(MismatchedInputException.class, () -> mapper.readValue("""
                {"id":"not-a-uuid","value":"22222222-2222-2222-2222-222222222222"}""",
                OperationKey.class));
    }

    @Test
    void invalidDateTime_throwsMismatchedInput() {
        assertThrows(MismatchedInputException.class, () -> mapper.readValue("""
                {"id":"11111111-1111-1111-1111-111111111111",
                 "value":"22222222-2222-2222-2222-222222222222",
                 "createdAt":"not-a-date"}""", OperationKey.class));
    }

    @Test
    void deserializesThroughLoginDTO() throws JacksonException {
        LoginDTO dto = mapper.readValue("""
                {"idempotencyKey":{"id":"11111111-1111-1111-1111-111111111111",
                 "value":"22222222-2222-2222-2222-222222222222"},
                 "name":"quitto","password":"senha123"}""", LoginDTO.class);

        assertNotNull(dto.idempotencyKey());
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                dto.idempotencyKey().getId());
        assertEquals(UUID.fromString("22222222-2222-2222-2222-222222222222"),
                dto.idempotencyKey().getValue());
    }
}