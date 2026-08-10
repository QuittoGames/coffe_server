package com.quitto.server.domain.interfaces.Token;

import com.quitto.server.domain.valueobject.Cookie.CookieDomain;
import java.util.Optional;

/**
 * Porta de domínio que abstrai o acesso a <strong>headers</strong> e
 * <strong>cookies</strong> de uma requisição, sem depender de framework web
 * ({@code HttpServletRequest}, Jakarta). Cada framework de entrada fornece um
 * adapter que implementa esta interface.
 */
public interface TokenRequestContext {

    Optional<String> getHeader(String name);

    Optional<CookieDomain> getCookie(String name);
}
