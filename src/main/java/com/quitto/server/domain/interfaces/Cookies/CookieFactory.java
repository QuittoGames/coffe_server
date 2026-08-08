package com.quitto.server.domain.interfaces.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

/**
 * Porta de fábrica de cookies (domínio puro).
 *
 * <p>Cria instâncias de {@link CookieDomain} com valores e parâmetros de
 * segurança, sem depender de qualquer framework web (Jakarta Servlet etc.).
 * A implementação concreta vive na infraestrutura — ex.: {@code CookieMapper}.</p>
 *
 * <p><strong>Por quê existe:</strong> a criação do cookie com defaults
 * seguros (HttpOnly, Secure, path) é um contrato de negócio; o domínio
 * define a porta e a infraestrutura converte para o cookie do framework
 * usado.</p>
 */
public interface CookieFactory {

    CookieDomain createCookie(String name, String value);

    CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds);

}
