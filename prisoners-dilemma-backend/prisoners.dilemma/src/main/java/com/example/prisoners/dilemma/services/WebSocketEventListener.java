package com.example.prisoners.dilemma.services;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class WebSocketEventListener {

    private final ConnectionService connectionService;

    /**
     * Maps the user id to the game id they are subscribed to
     */
    private final Map<String, String> userSubscription = new HashMap<>();

    public WebSocketEventListener(ConnectionService connectionService){
        this.connectionService = connectionService;
    }

    @EventListener
    public void webSocketDisconnectionListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String gameIdToWhichPlayerWasConnected = userSubscription.remove(headerAccessor.getUser().getName());
        connectionService.deleteGameAndNotifyUser(UUID.fromString(gameIdToWhichPlayerWasConnected));
    }

    /**
     * Called whenever a player subscribes to a game
     * @param event
     */
    @EventListener
    public void gameConnectionListener(SessionSubscribeEvent event){

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        UUID gameId;
        try{
            gameId = UUID.fromString(getGameId(headerAccessor));
        } catch (IllegalArgumentException ex){
            // not uuid, player is subscribing to user queue, not to a game
            return;
        }
        if(event.getUser() instanceof OAuth2AuthenticationToken token){
            userSubscription.put(token.getPrincipal().getName(), gameId.toString());
            connectionService.playerConnectedToGame(gameId, UUID.fromString(token.getPrincipal().getName()));
        }
    }

    private String getGameId(StompHeaderAccessor headerAccessor) {
        String[] topicPathSplit = headerAccessor.getDestination().split("/");
        return topicPathSplit[topicPathSplit.length - 1];
    }
}