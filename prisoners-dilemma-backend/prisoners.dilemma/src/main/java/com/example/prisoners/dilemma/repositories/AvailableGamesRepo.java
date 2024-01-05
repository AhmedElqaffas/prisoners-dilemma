package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.dtos.GameAndConnectedPlayers;
import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.entities.Player;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AvailableGamesRepo {

    private final List<GameAndConnectedPlayers> availableGames = new ArrayList<>();
    public Collection<GameAndConnectedPlayers> getAllAvailableGames() {
        return availableGames;
    }

    public void createNewGame(Game game, Player creator) {
        GameAndConnectedPlayers gameAndPlayers = new GameAndConnectedPlayers(game);
        gameAndPlayers.addPlayer(creator);
        availableGames.add(gameAndPlayers);
    }

    /**
     * Deletes a game from the available games that a player can connect to.
     *
     * @param gameId
     * @return true, if any game was removed
     */
    public boolean deleteGame(UUID gameId) {
        return availableGames
                .removeIf(game -> game.getGame().getId().equals(gameId));
    }

    public Optional<GameAndConnectedPlayers> getGameAndPlayers(UUID gameId) {
        return availableGames.stream().filter(game -> game.getGame().getId().equals(gameId))
                .findFirst();
    }
}
