package com.example.prisoners.dilemma.controllers;

import com.example.prisoners.dilemma.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class ConnectionController {

    private final ConnectionService connectionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    public ConnectionController(ConnectionService connectionService){
        this.connectionService = connectionService;
    }

    @MessageMapping("search")
    public void searchForGame(
            Principal user
    ){
        //simpMessagingTemplate.convertAndSendToUser(user.getName(), "/queue/specific-user","PONG");
        connectionService.searchForGame(user.getName());
    }
}
