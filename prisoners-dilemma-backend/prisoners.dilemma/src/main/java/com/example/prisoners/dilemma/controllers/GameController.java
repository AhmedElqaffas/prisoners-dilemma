package com.example.prisoners.dilemma.controllers;

import com.example.prisoners.dilemma.dtos.Choice;
import com.example.prisoners.dilemma.exceptions.PlayerAlreadyPlayedException;
import com.example.prisoners.dilemma.services.GameService;
import com.example.prisoners.dilemma.services.WebSocketMessageSender;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Controller
public class GameController {

    private static final String PLAYER_ALREADY_PLAYED = "PLAYER_ALREADY_PLAYED";

    private GameService gameService;
    private final WebSocketMessageSender webSocketMessageSender;
    public GameController(GameService gameService,
                          WebSocketMessageSender webSocketMessageSender){
        this.gameService = gameService;
        this.webSocketMessageSender = webSocketMessageSender;
    }
    @MessageMapping("/topic/game/{gameId}")
    public void play(@RequestBody ChoiceDTO playerChoice, @DestinationVariable String gameId, OAuth2AuthenticationToken oAuthToken) {
        if(oAuthToken == null){
            return;
        }
        UUID playerId = UUID.fromString(oAuthToken.getPrincipal().getName());
        try{
            gameService.playerPlayed(playerId, playerChoice.choice(), UUID.fromString(gameId));
        } catch (PlayerAlreadyPlayedException ex){
            webSocketMessageSender.sendToUser(playerId.toString(), PLAYER_ALREADY_PLAYED);
        }
    }

    record ChoiceDTO(Choice choice) { }
}