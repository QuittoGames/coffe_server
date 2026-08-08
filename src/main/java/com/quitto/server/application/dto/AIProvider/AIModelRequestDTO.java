package com.quitto.server.application.dto.AIProvider;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO do endpoint {@code GET /coffee/api/v1/ai/provider/models/{model}}.
 *
 * <p>Identifica um modelo de IA pelo seu id (path variable).</p>
 */
public record AIModelRequestDTO(
        @NotBlank String model
) {
}
