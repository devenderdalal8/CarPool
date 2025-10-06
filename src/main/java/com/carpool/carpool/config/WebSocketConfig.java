package com.carpool.carpool.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /*
    * This class configures WebSocket messaging using STOMP protocol.
    * STOMP endpoints:
    * Send: /app/message
    * Subscribe: /topic/messages
    * Message Broker: /topic
    * Application Prefix: /app
    * Endpoint: /ws
    * Client library: SockJS
    * */
    @Override
    public void configureMessageBroker(@NotNull MessageBrokerRegistry config) {
//       Enables a simple in-memory message broker for destinations prefixed
//       with '/topic' (used for broadcasting messages to subscribers)
        config.enableSimpleBroker("/topic" , "/private");
        // Enables /user/{username}/queue/... destinations
        config.setUserDestinationPrefix("/user");
        //topic subscribe which is use for publish my topic
        // Set prefix for messages sent from client to server (e\.g\., client sends to /app/xyz, server handles it)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-editor") // connection establishment
                .setAllowedOrigins("*").withSockJS();
    }
}
