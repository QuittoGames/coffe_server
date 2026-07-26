package com.quitto.server.domain.interfaces.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

public interface CookieFactory {

    CookieDomain createCookie(String name, String value);

    CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds);

}
