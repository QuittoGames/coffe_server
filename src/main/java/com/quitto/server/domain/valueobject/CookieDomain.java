package com.quitto.server.domain.valueobject;

import java.util.Objects;

/**
 * Value Object imutável que representa um cookie HTTP, independente de framework.
 * Auto-validável (RFC 6265) e comparável por valor. Os factory methods {@code of(...)}
 * criam cookies com defaults seguros (httpOnly + secure).
 */
public record CookieDomain(
    String name,
    String value,
    boolean httpOnly,
    boolean secure,
    String path,
    Integer maxAge
) {
    public CookieDomain {
        Objects.requireNonNull(name, "Cookie name must not be null");
        Objects.requireNonNull(value, "Cookie value must not be null");

        // Basic validation - name cannot contain certain characters per RFC 6265
        if (name.isEmpty() || name.contains("=") || name.contains(";") || name.contains(",")) {
            throw new IllegalArgumentException("Invalid cookie name: " + name);
        }
    }

    public static CookieDomain of(String name, String value) {
        return new CookieDomain(name, value, true, true, "/", null);
    }

    public static CookieDomain of(String name, String value, String path, Integer maxAgeInSeconds) {
        return new CookieDomain(name, value, true, true, path, maxAgeInSeconds);
    }
}
