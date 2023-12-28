package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.entities.Game;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AvailableGamesRepo {

    private final List<Game> availableGames = new ArrayList<>();
    public Collection<Game> getAllAvailableGames() {
        return availableGames;
    }

    public Game createNewGame() {
        Game game =  new Game();
        availableGames.add(game);
        return game;
    }

    public void deleteGame(String gameId) {
        availableGames.removeIf(game -> game.getId().toString().equals(gameId));
    }
}
