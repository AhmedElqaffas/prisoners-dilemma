package com.example.prisoners.dilemma.controllers;

import com.example.prisoners.dilemma.services.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/connection")
public class ConnectionController {

    private final ConnectionService connectionService;


    public ConnectionController(ConnectionService connectionService){
        this.connectionService = connectionService;
    }

    @PostMapping("search")
    public ResponseEntity<String> searchForGame(Principal user){
        UUID gameId = connectionService.searchForGame(user);
        if(gameId == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gameId.toString());
    }
}
