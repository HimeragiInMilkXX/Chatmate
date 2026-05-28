package com.easyprofile.demo.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Component
public class SaTokenPrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
        ServerHttpRequest request,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes
    ) {
        Object loginId = attributes.get(SaTokenHandshakeInterceptor.ATTR_LOGIN_ID);
        String principalName = loginId == null ? null : String.valueOf(loginId);
        if (principalName == null || principalName.isBlank()) {
            return null;
        }
        return () -> principalName;
    }
}
