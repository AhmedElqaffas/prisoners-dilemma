package com.example.prisoners.dilemma.dtos;

import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class GameAndConnectedPlayers {
    private Game game;
    private final List<Player> players = new ArrayList<>(2);

    public GameAndConnectedPlayers(Game game){
        this.game = game;
    }

    public void addPlayer(Player player) {
        if(players.size() < 2){
            players.add(player);
        }
    }

    public Game getGame() {
        return game;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
