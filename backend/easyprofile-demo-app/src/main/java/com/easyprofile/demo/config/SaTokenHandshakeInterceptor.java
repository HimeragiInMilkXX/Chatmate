package com.easyprofile.demo.config;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class SaTokenHandshakeInterceptor implements HandshakeInterceptor {

    public static final String ATTR_LOGIN_ID = "loginId";
    private static final String TOKEN_NAME = "satoken";

    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes
    ) {
        if (!(request instanceof ServletServerHttpRequest servletServerHttpRequest)) {
            return false;
        }

        String token = resolveToken(servletServerHttpRequest.getServletRequest());
        if (token == null || token.isBlank()) {
            return false;
        }

        Object loginId = StpUtil.getLoginIdByToken(token);
        if (loginId == null) {
            return false;
        }

        attributes.put(ATTR_LOGIN_ID, String.valueOf(loginId));
        return true;
    }

    @Override
    public void afterHandshake(
        ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Exception exception
    ) {
    }

    private String resolveToken(HttpServletRequest request) {
        String fromCookie = readCookie(request);
        if (fromCookie != null && !fromCookie.isBlank()) {
            return fromCookie;
        }

        String fromHeader = request.getHeader(TOKEN_NAME);
        if (fromHeader != null && !fromHeader.isBlank()) {
            return fromHeader;
        }

        String fromQuery = request.getParameter(TOKEN_NAME);
        if (fromQuery != null && !fromQuery.isBlank()) {
            return URLDecoder.decode(fromQuery, StandardCharsets.UTF_8);
        }

        return null;
    }

    private String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (TOKEN_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
