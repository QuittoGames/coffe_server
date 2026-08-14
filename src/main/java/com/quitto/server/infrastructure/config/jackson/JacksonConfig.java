package com.quitto.server.infrastructure.config.jackson;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.module.SimpleModule;

import com.quitto.server.domain.interfaces.OperationKey.OperationKey;

/**
 * Registra o {@link OperationKeyDeserializer} no ObjectMapper gerenciado pelo
 * Spring (Jackson 3), permitindo que clientes HTTP enviem
 * {@code idempotencyKey} não-nula via JSON nos DTOs de autenticação.
 *
 * <p>
 * O domínio permanece puro — nenhuma anotação Jackson em
 * {@code OperationKey}/{@code IdempotencyKey}.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer operationKeyJsonMapperCustomizer() {
        return builder -> builder.addModule(new SimpleModule()
                .addDeserializer(OperationKey.class, new OperationKeyDeserializer()));
    }
}