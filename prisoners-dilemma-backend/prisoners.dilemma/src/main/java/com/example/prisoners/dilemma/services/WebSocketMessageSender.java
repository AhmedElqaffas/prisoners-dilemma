package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.configurations.WebSocketConfig;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMessageSender {
    private final SimpMessagingTemplate simpMessagingTemplate;
    public WebSocketMessageSender(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    public void sendToSubscribers(String gameId, Object message){
        simpMessagingTemplate.convertAndSend(WebSocketConfig.GAME_TOPIC_PATH + "/" + gameId , message);
    }

    public void sendToUser(String userId, Object message){
        simpMessagingTemplate.convertAndSendToUser(userId, WebSocketConfig.USER_QUEUE_PATH,message);
    }
}
