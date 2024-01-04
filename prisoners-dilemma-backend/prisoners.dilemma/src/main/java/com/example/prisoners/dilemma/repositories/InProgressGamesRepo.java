package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.dtos.GameAndConnectedPlayers;
import com.example.prisoners.dilemma.entities.PlayerChoice;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InProgressGamesRepo {
    private final Map<UUID, GameAndChoicesDTO> inProgressGames = new HashMap<>();

    public void add(GameAndConnectedPlayers gameAndPlayers) {
        inProgressGames.put(gameAndPlayers.getGame().getId(), new GameAndChoicesDTO(gameAndPlayers));
    }

    public void remove(UUID gameId){
        inProgressGames.remove(gameId);
    }

    public GameAndChoicesDTO get(UUID gameId) {
        return inProgressGames.get(gameId);
    }

    public void saveChoice(UUID gameId, PlayerChoice playerChoice) {
        GameAndChoicesDTO gameAndChoices = inProgressGames.get(gameId);
        if(gameAndChoices == null){
            return;
        }

        gameAndChoices.addChoice(playerChoice);
    }

    public static class GameAndChoicesDTO {
        private GameAndConnectedPlayers gameAndPlayers;
        private List<PlayerChoice> gameChoices = new ArrayList<>(2);

        public GameAndChoicesDTO(GameAndConnectedPlayers gameAndPlayers){
            this.gameAndPlayers = gameAndPlayers;
        }

        public GameAndConnectedPlayers getGameAndPlayers() {
            return gameAndPlayers;
        }

        public void addChoice(PlayerChoice choice){
            gameChoices.add(choice);
        }

        public List<PlayerChoice> getGameChoices() {
            return gameChoices;
        }
    }
}
