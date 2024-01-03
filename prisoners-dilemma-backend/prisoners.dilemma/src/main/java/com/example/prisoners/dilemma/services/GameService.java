package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.dtos.Choice;
import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.repositories.GamesRepo;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {

    private final GamesRepo gamesRepo;

    public GameService(GamesRepo gamesRepo){
        this.gamesRepo = gamesRepo;
    }


    public void startGame(Game game) {
        gamesRepo.save(game);
    }

    public void playerPlayed(UUID playerId, Choice choice, String gameId) {

    }
}
