package com.example.prisoners.dilemma.controllers;

import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.services.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connection")
public class ConnectionController {

    private final ConnectionService connectionService;


    public ConnectionController(ConnectionService connectionService){
        this.connectionService = connectionService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("search")
    public ResponseEntity<String> searchForGame(){
        Game game = connectionService.searchForGame();
        return ResponseEntity.ok(game.getId().toString());
    }
}
