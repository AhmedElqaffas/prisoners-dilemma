package com.example.prisoners.dilemma.dtos;

import com.example.prisoners.dilemma.entities.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameAndConnectedPlayers {
    private Game game;
    private final List<UUID> players = new ArrayList<>(2);

    public GameAndConnectedPlayers(Game game){
        this.game = game;
    }

    public void addPlayer(UUID player) {
        if(players.size() < 2){
            players.add(player);
        }
    }

    public Game getGame() {
        return game;
    }

    public List<UUID> getPlayers() {
        return players;
    }
}
