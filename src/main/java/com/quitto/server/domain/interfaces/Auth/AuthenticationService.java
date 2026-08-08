package com.quitto.server.domain.interfaces.Auth;

import com.quitto.server.domain.exception.AuthenticationException;
import com.quitto.server.domain.models.User.User;

/**
 * Porta de domínio para <strong>autenticação e registro de usuários</strong>.
 *
 * <p>Define o contrato de login e cadastro do agregado {@link User}. A
 * implementação concreta vive na infraestrutura
 * ({@code SpringAuthenticationService}) e adapta o Spring Security
 * ({@code AuthenticationManager}) para este contrato, traduzindo exceções do
 * framework para {@link AuthenticationException} do domínio.</p>
 *
 * <p><strong>Coesão:</strong> autenticar e registrar vivem na mesma porta por
 * serem operações complementares do mesmo agregado ({@code User}).</p>
 *
 * @see com.quitto.server.domain.models.User.User
 * @see com.quitto.server.domain.exception.AuthenticationException
 */
public interface AuthenticationService{

    User authenticate(String username, String password) throws AuthenticationException;

    User register(String name, String password , String email);
}
