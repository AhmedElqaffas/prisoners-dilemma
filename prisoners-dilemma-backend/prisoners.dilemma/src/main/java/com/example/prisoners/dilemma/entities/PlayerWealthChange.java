package com.example.prisoners.dilemma.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "players_wealth_changes")
public class PlayerWealthChange {
    @EmbeddedId
    private PlayerWealthChange.PK playerAndGame;
    private int deltaWealth;

    public PlayerWealthChange(){}

    public PlayerWealthChange(PlayerWealthChange.PK playerAndGame, int deltaWealth){
        this.playerAndGame = playerAndGame;
        this.deltaWealth = deltaWealth;
    }

    public int getDeltaWealth() {
        return deltaWealth;
    }

    public static class PK implements Serializable {

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

        public PK(){}

        public PK(Player player, Game game){
            this.player = player;
            this.game = game;
        }
    }
}
