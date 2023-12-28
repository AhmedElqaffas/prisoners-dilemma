package com.example.prisoners.dilemma.controllers;

import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.services.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/connection")
public class ConnectionController {

    private final ConnectionService connectionService;


    public ConnectionController(ConnectionService connectionService){
        this.connectionService = connectionService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("search")
    public ResponseEntity<String> searchForGame(Principal user){
        Game game = connectionService.searchForGame(user);
        return ResponseEntity.ok(game.getId().toString());
    }
}

record Temp(String id) {

}
