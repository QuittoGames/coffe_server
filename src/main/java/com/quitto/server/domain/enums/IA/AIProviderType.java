package com.quitto.server.domain.enums.IA;

/**
 * Identidade tipada de um provedor de IA.
 *
 * <p>Substitui o enum genérico {@code ServiceProvider} como chave de indexação
 * do registry e como identidade de {@code AIProvider.getProvider()} e
 * {@code AIModel.provider}. O conjunto é reduzido e extensível: novos provedores
 * entram como constantes novas, sem afetar o contrato.</p>
 */
public enum AIProviderType {

    OPENAI,
    OPENROUTER,
    GEMINI
}