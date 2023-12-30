package com.example.prisoners.dilemma.services;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashMap;
import java.util.Map;

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
        connectionService.deleteGameAndNotifyUser(gameIdToWhichPlayerWasConnected);
    }

    /**
     * Called whenever a player subscribes to a game
     * @param event
     */
    @EventListener
    public void gameConnectionListener(SessionSubscribeEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String gameId = getGameId(headerAccessor);

        userSubscription.put(headerAccessor.getUser().getName(), gameId);
        connectionService.playerConnectedToGame(gameId);
    }

    private String getGameId(StompHeaderAccessor headerAccessor) {
        String[] topicPathSplit = headerAccessor.getDestination().split("/");
        return topicPathSplit[topicPathSplit.length - 1];
    }
}