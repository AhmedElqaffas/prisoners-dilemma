package com.example.prisoners.dilemma.controllers;

import com.example.prisoners.dilemma.dtos.OAuth2UserWithId;
import com.example.prisoners.dilemma.entities.Player;
import com.example.prisoners.dilemma.services.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    @GetMapping("player")
    public ResponseEntity<Player> getPlayerDetails(OAuth2AuthenticationToken oAuthToken){
        if(oAuthToken == null || !(oAuthToken.getPrincipal() instanceof OAuth2UserWithId)){
            return ResponseEntity.status(401).build();
        }

        UUID playerId = ((OAuth2UserWithId) oAuthToken.getPrincipal()).getId();

        Optional<Player> player = playerService.getPlayerDetails(playerId);
        return player.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
