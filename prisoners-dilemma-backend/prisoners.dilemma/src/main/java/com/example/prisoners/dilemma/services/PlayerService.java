package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.dtos.PlayerGameResultDTO;
import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.entities.Player;
import com.example.prisoners.dilemma.entities.PlayerWealthChange;
import com.example.prisoners.dilemma.entities.PrisonerDilemmaAuthUser;
import com.example.prisoners.dilemma.repositories.PlayerWealthChangesRepo;
import com.example.prisoners.dilemma.repositories.PlayersRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    private PlayersRepo playerRepo;
    private PlayerWealthChangesRepo wealthChangesRepo;
    public PlayerService(PlayersRepo playerRepo, PlayerWealthChangesRepo wealthChangesRepo){
        this.playerRepo = playerRepo;
        this.wealthChangesRepo = wealthChangesRepo;
    }

    public Optional<Player> getPlayerDetails(UUID playerId){
        if(playerId == null){
            return Optional.empty();
        }

        return playerRepo.findById(playerId);
    }

    public void createPlayer(PrisonerDilemmaAuthUser authUser) {
        playerRepo.save(Player.of(authUser));
    }

    public void updatePlayerWealth(Player player, PlayerGameResultDTO playerGameResult, Game game) {
        updateWealthInPlayersTable(playerGameResult, player);
        addDeltaWealthInPlayersWealthChangesTable(playerGameResult, player, game);



    }
    private void updateWealthInPlayersTable(PlayerGameResultDTO playerGameResult, Player player) {
        player.setWealth(player.getWealth() + playerGameResult.getMoneyGained());
        playerRepo.save(player);
    }

    private void addDeltaWealthInPlayersWealthChangesTable(PlayerGameResultDTO playerGameResult, Player player, Game game) {
        var wealthChange = new PlayerWealthChange(new PlayerWealthChange.PK(player, game), playerGameResult.getMoneyGained());
        wealthChangesRepo.save(wealthChange);
    }


}
