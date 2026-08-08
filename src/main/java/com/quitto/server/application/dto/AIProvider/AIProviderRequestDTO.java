package com.quitto.server.application.dto.AIProvider;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO do endpoint {@code GET /coffee/api/v1/ai/provider/provaiders/{provaider}}.
 *
 * <p>Identifica um provedor de IA pelo nome/enum (path variable).</p>
 */
public record AIProviderRequestDTO(
        @NotBlank String provaider
) {
}
