package com.example.prisoners.dilemma.controllers;

import com.example.prisoners.dilemma.services.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/connection")
public class ConnectionController {

    private final ConnectionService connectionService;


    public ConnectionController(ConnectionService connectionService){
        this.connectionService = connectionService;
    }

    @PostMapping("search")
    public ResponseEntity<String> searchForGame(OAuth2AuthenticationToken user){
        UUID gameId = connectionService.searchForGame(user);
        if(gameId == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gameId.toString());
    }
}
