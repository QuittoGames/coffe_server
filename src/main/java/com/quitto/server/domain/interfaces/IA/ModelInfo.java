package com.quitto.server.domain.interfaces.IA;

/**
 * Value object que representa um modelo disponível em um provedor de IA.
 *
 * <p>Estrutura mínima e agnóstica de provedor: {@code id} identifica o modelo
 * (chamado de {@code name} por alguns provedores, como Google/Cohere) e os
 * campos criado/proprietário são opcionais e nem sempre preenchidos.</p>
 *
 * @param id       identificador do modelo (ex.: {@code gpt-4o}, {@code models/gemini-2.0-flash})
 * @param created  timestamp Unix de criação, quando o provedor informar
 * @param ownedBy  organização proprietária, quando o provedor informar
 */
public record ModelInfo(
        String id,
        Long created,
        String ownedBy) {
}