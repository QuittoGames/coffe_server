package com.quitto.server.domain.enums;

/**
 * Papéis (roles) do sistema de permissões do Coffee Server.
 *
 * <p>Define os níveis de acesso de um {@code User}: <strong>ADMIN</strong>
 * (acesso total), <strong>USER</strong> (padrão), <strong>MCP</strong>
 * (endpoints MCP de agentes de IA) e <strong>API</strong> (APIs específicas).
 * As authorities do Spring Security derivam do nome do enum.</p>
 *
 * @see com.quitto.server.domain.models.User.User
 */
public enum Role {
    ADMIN,
    USER,
    MCP,
    API
}
