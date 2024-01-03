package com.example.prisoners.dilemma.controllers;

import com.example.prisoners.dilemma.dtos.ChoiceDTO;
import com.example.prisoners.dilemma.dtos.OAuth2UserWithId;
import com.example.prisoners.dilemma.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Controller
public class GameController {

    private GameService gameService;
    public GameController(GameService gameService){
        this.gameService = gameService;
    }
    @MessageMapping("/topic/game/{gameId}")
    public ResponseEntity play(@RequestBody ChoiceDTO playerChoice, @DestinationVariable String gameId, OAuth2AuthenticationToken oAuthToken) throws Exception {
        if(oAuthToken == null || !(oAuthToken.getPrincipal() instanceof OAuth2UserWithId)){
            return ResponseEntity.status(401).build();
        }
        UUID playerId = ((OAuth2UserWithId) oAuthToken.getPrincipal()).getId();
        gameService.playerPlayed(playerId, playerChoice.choice(), gameId);

        return ResponseEntity.ok().build();
    }
}