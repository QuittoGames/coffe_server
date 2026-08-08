package com.quitto.server.domain.interfaces.Auth;

/**
 * Porta de domínio para <strong>hashing e verificação de senhas</strong>.
 *
 * <p>Define o contrato mínimo de um serviço de senha: codificar uma senha crua
 * em um hash seguro (ex.: BCrypt) e comparar uma senha crua com um hash já
 * armazenado. A implementação concreta vive na infraestrutura
 * ({@code BCryptPasswordService}) e usa o BCrypt do Spring Security.</p>
 *
 * <p>O domínio nunca lida com senha em texto puro — apenas com o hash gerado
 * por esta porta.</p>
 */
public interface PasswordService {

    String encode(String raw);

    boolean matches(String raw , String passwordHash);
}
