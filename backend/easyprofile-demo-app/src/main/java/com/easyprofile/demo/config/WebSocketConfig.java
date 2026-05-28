package com.easyprofile.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SaTokenHandshakeInterceptor saTokenHandshakeInterceptor;
    private final SaTokenPrincipalHandshakeHandler saTokenPrincipalHandshakeHandler;

    public WebSocketConfig(
        SaTokenHandshakeInterceptor saTokenHandshakeInterceptor,
        SaTokenPrincipalHandshakeHandler saTokenPrincipalHandshakeHandler
    ) {
        this.saTokenHandshakeInterceptor = saTokenHandshakeInterceptor;
        this.saTokenPrincipalHandshakeHandler = saTokenPrincipalHandshakeHandler;
    }

    // Defines how messages are routed after connection
    @Override
    public void configureMessageBroker( MessageBrokerRegistry config ) {

        // Define router than frontend can subscribe to
        config.enableSimpleBroker( "/topic", "/queue" );

        // Messages starting with this prefix should be routed to @MessageMapping methods
        config.setApplicationDestinationPrefixes( "/app" );

    }

    // Define where the frontend connects, which here is /ws-chat
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(saTokenHandshakeInterceptor)
                .setHandshakeHandler(saTokenPrincipalHandshakeHandler)
                .withSockJS();
    }

}
