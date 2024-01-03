package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.entities.Player;
import com.example.prisoners.dilemma.entities.PrisonerDilemmaAuthUser;
import com.example.prisoners.dilemma.repositories.PlayersRepo;
import com.example.prisoners.dilemma.repositories.UserAuthRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    private PlayersRepo playerRepo;
    private UserAuthRepo authRepo;
    public PlayerService(PlayersRepo playerRepo, UserAuthRepo authRepo){
        this.playerRepo = playerRepo;
        this.authRepo = authRepo;
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
}
