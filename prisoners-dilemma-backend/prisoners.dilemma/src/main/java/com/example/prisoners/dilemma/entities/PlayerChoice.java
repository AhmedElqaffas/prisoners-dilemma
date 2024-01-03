package com.example.prisoners.dilemma.entities;

import com.example.prisoners.dilemma.dtos.Choice;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "games_results")
public class PlayerChoice{

    @EmbeddedId
    private GameResultId playerAndGame;
    private Choice choice;

    public PlayerChoice(){}

    public PlayerChoice(GameResultId playerAndGame, Choice choice){
        this.playerAndGame = playerAndGame;
        this.choice = choice;
    }

    public Game getGame(){
        return playerAndGame.game;
    }

    public Player getPlayer(){
        return playerAndGame.player;
    }

    public Choice getChoice() {
        return choice;
    }

    public static class GameResultId implements Serializable {

        @ManyToOne
        @JoinColumn(
                nullable = false,
                name = "player",
                foreignKey = @ForeignKey(name = "game_result_player",
                        foreignKeyDefinition = "FOREIGN KEY (player) REFERENCES public.players(id) ON DELETE CASCADE")
        )
        private Player player;

        @ManyToOne
        @JoinColumn(
                nullable = false,
                name = "game",
                foreignKey = @ForeignKey(name = "game_result_game",
                        foreignKeyDefinition = "FOREIGN KEY (game) REFERENCES public.games(id) ON DELETE CASCADE")
        )
        private Game game;

        public GameResultId(){}

        public GameResultId(Player player, Game game){
            this.player = player;
            this.game = game;
        }
    }


}
