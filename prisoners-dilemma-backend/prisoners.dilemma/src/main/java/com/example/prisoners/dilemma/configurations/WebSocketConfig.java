package com.example.prisoners.dilemma.configurations;

import com.sun.security.auth.UserPrincipal;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/queue");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/game-connection-ws")
                    .setHandshakeHandler(new DefaultHandshakeHandler(){
                        @Override
                        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                            return new UserPrincipal(UUID.randomUUID().toString());
                        }
                    })
                    .setAllowedOriginPatterns("*");
    }

}