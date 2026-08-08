package com.quitto.server.domain.enums;

/**
 * Políticas de <strong>rate limiting</strong> por tipo de rota.
 *
 * <p>Distingue o tratamento de limite de requisições conforme a natureza do
 * endpoint: <strong>LOGIN</strong> (autenticação), <strong>API</strong>
 * (rotas em geral) e <strong>UPLOAD</strong> (upload de arquivos). É o
 * parâmetro do contrato de rate limit ({@code RateLimit.tryConsume(key, policy)})
 * e é consumida pela infraestrutura (Bucket4j + Redis).</p>
 */
public enum RateLimitPolicy {
    LOGIN,
    API,
    UPLOAD
}
