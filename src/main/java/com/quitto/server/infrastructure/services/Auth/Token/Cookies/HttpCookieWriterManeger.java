package com.quitto.server.infrastructure.services.Auth.Token.Cookies;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import com.quitto.server.application.interfaces.Cookies.HttpCookieWriter;
import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.interfaces.Cookies.HttpCookieMapper;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

@Service
public class HttpCookieWriterManeger implements HttpCookieWriter{

    private final HttpCookieMapper cookieMapper;

    public HttpCookieWriterManeger(HttpCookieMapper cookieMapper){
		this.cookieMapper = cookieMapper;
	}

	@Override
    public void writeCookie(HttpServletResponse response, CookieDomain cookieDomain) {
        Objects.requireNonNull(response, "response cannot be null");
        Objects.requireNonNull(cookieDomain, "cookieDomain cannot be null");

        ResponseCookie.ResponseCookieBuilder builder = cookieMapper.toFrameworkCookie(cookieDomain);

        if (cookieDomain.maxAge() != null) {
            builder.maxAge(cookieDomain.maxAge());
        }

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }
}
