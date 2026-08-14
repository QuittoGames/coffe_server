package com.quitto.server.infrastructure.config.jackson;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;

import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.valueobject.IdempotencyKey.IdempotencyKey;

/**
 * Deserializador da porta {@link OperationKey} para o ObjectMapper do Spring
 * (Jackson 3). Suporta o wire format:
 *
 * <pre>
 * {
 *   "id": "&lt;uuid&gt;",
 *   "value": "&lt;uuid&gt;",
 *   "createdAt": "&lt;ISO-8601 opcional&gt;",
 *   "expiresAt": "&lt;ISO-8601 opcional&gt;"
 * }
 * </pre>
 *
 * <p>
 * {@code createdAt}/{@code expiresAt} aceitam {@code LocalDateTime} (sem
 * offset) ou {@code Instant} (com offset/Z). Ausentes, assumem agora (UTC) e
 * agora + 10 minutos, respectivamente — os mesmos defaults da
 * {@code IdempotencyKeyFactory}. {@code id}/{@code value} ausentes ou com UUID
 * inválido falham com erro de desserialização (HTTP 400).
 */
public class OperationKeyDeserializer extends ValueDeserializer<OperationKey> {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);

    @Override
    public OperationKey deserialize(JsonParser parser, DeserializationContext context)
            throws JacksonException {
        JsonNode node = context.readTree(parser);

        UUID id = requiredUuid(node, "id", context);
        UUID value = requiredUuid(node, "value", context);

        LocalDateTime createdAt = optionalDateTime(node, "createdAt",
                LocalDateTime.now(ZoneOffset.UTC), context);
        LocalDateTime expiresAt = optionalDateTime(node, "expiresAt",
                createdAt.plus(DEFAULT_TTL), context);

        return new IdempotencyKey(id, value, createdAt, expiresAt);
    }

    private static UUID requiredUuid(JsonNode node, String field, DeserializationContext context)
            throws JacksonException {
        JsonNode value = node.get(field);
        if (value == null || value.isNull() || value.asString().isBlank()) {
            throw MismatchedInputException.from(context.getParser(), OperationKey.class,
                    String.format("OperationKey field '%s' is required", field));
        }
        String raw = value.asString();
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException e) {
            throw MismatchedInputException.from(context.getParser(), OperationKey.class,
                    String.format("OperationKey field '%s' must be a valid UUID, got: %s",
                            field, raw));
        }
    }

    private static LocalDateTime optionalDateTime(JsonNode node, String field,
            LocalDateTime fallback, DeserializationContext context) throws JacksonException {
        JsonNode value = node.get(field);
        if (value == null || value.isNull() || value.asString().isBlank()) {
            return fallback;
        }
        String raw = value.asString();
        try {
            return LocalDateTime.parse(raw);
        } catch (DateTimeParseException e) {
            // tenta como Instant (com offset/Z)
        }
        try {
            return Instant.parse(raw).atZone(ZoneOffset.UTC).toLocalDateTime();
        } catch (DateTimeParseException e) {
            throw MismatchedInputException.from(context.getParser(), OperationKey.class,
                    String.format(
                            "OperationKey field '%s' must be an ISO-8601 date-time, got: %s",
                            field, raw));
        }
    }
}