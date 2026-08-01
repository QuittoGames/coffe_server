package com.quitto.server.application.interfaces.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

import jakarta.servlet.http.HttpServletResponse;

/**
 * SPI de fronteira de framework: escreve um {@link CookieDomain} na resposta HTTP.
 *
 * <p>Pertence à camada de application porque é consumida pelo controller
 * (que não deve depender de infraestrutura). A implementação concreta
 * (Jakarta Servlet / Spring) vive na infraestrutura.</p>
 */
public interface HttpCookieWriter {
    void writeCookie(HttpServletResponse response, CookieDomain cookieDomain);
}
