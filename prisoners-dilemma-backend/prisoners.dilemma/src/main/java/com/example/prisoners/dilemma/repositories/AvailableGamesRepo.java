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

    public Game createNewGame(UUID gameId) {
        Game game =  new Game(gameId);
        availableGames.add(game);
        return game;
    }

    /**
     * Deletes a game from the available games that a player can connect to.
     *
     * @param gameId
     * @return true, if any game was removed
     */
    public boolean deleteGame(String gameId) {
        return availableGames.removeIf(game -> game.getId().toString().equals(gameId));
    }
}
