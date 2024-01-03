package com.example.prisoners.dilemma.dtos;

import com.example.prisoners.dilemma.entities.Game;

import java.io.Serializable;
import java.util.UUID;

//@Entity
//@Table(name = "games_results")
public class PlayerChoice{

    //@EmbeddedId
    private GameResultId playerAndGame;
    private Choice choice;

    public PlayerChoice(){}

    public PlayerChoice(GameResultId playerAndGame, Choice choice){
        this.playerAndGame = playerAndGame;
        this.choice = choice;
    }

    public static class GameResultId implements Serializable {

        private UUID playerId;

/*        @ManyToOne
        @JoinColumn(
                nullable = false,
                name = "game",
                foreignKey = @ForeignKey(name = "game_result_game",
                        foreignKeyDefinition = "FOREIGN KEY (games) REFERENCES public.games(id) ON DELETE CASCADE")
        )*/
        private Game game;

        public GameResultId(){}

        public GameResultId(UUID playerId, Game game){
            this.playerId = playerId;
            this.game = game;
        }
    }


}
