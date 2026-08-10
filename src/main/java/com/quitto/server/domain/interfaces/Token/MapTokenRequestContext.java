package com.quitto.server.domain.interfaces.Token;

import com.quitto.server.domain.valueobject.Cookie.CookieDomain;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação <strong>em memória</strong> de {@link TokenRequestContext},
 * baseada em {@link Map}s. Serve para testes unitários e uso sem framework web.
 * Mapas {@code null} passados ao construtor viram mapas vazios imutáveis.
 */
public class MapTokenRequestContext implements TokenRequestContext {

    private final Map<String, String> headers;
    private final Map<String, CookieDomain> cookies;

    public MapTokenRequestContext(Map<String, String> headers, Map<String, CookieDomain> cookies) {
        this.headers = headers != null ? headers : Map.of();
        this.cookies = cookies != null ? cookies : Map.of();
    }

    @Override
    public Optional<String> getHeader(String name) {
        return Optional.ofNullable(headers.get(name));
    }

    @Override
    public Optional<CookieDomain> getCookie(String name) {
        return Optional.ofNullable(cookies.get(name));
    }
}
