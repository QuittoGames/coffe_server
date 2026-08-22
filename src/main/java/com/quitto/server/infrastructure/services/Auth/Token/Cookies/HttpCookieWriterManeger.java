package com.quitto.server.infrastructure.services.Auth.Token.Cookies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quitto.server.application.interfaces.Cookies.HttpCookieWriter;
import com.quitto.server.domain.valueobject.Cookie.CookieDomain;
import com.quitto.server.infrastructure.interfaces.Cookies.HttpCookieMapper;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class HttpCookieWriterManeger implements HttpCookieWriter {

    @Autowired
    private HttpCookieMapper cookieMapper;

    @Override
    public void writeCookie(HttpServletResponse response, CookieDomain cookieDomain) {
        // TODO: Implement original logic using cookieMapper to convert CookieDomain to HttpServletResponse cookie
        // This is a placeholder - the actual implementation should be restored based on original code
        if (cookieMapper != null && cookieDomain != null && response != null) {
            // Original implementation would go here
            // Example: ResponseCookie.ResponseCookieBuilder builder = cookieMapper.toFrameworkCookie(cookieDomain);
            //          // then add cookie to response
        }
    }
}