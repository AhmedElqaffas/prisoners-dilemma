package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.dtos.Choice;
import com.example.prisoners.dilemma.dtos.GameAndConnectedPlayers;
import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.entities.Player;
import com.example.prisoners.dilemma.entities.PlayerChoice;
import com.example.prisoners.dilemma.exceptions.PlayerAlreadyPlayedException;
import com.example.prisoners.dilemma.repositories.GamesRepo;
import com.example.prisoners.dilemma.repositories.InProgressGamesRepo;
import com.example.prisoners.dilemma.repositories.PlayersChoicesRepo;
import com.example.prisoners.dilemma.repositories.PlayersRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {

    private static final String GAME_CONCLUDED = "GAME_CONCLUDED";
    private final GamesRepo gamesRepo;
    private final PlayersRepo playersRepo;
    private final PlayersChoicesRepo playerChoiceRepo;
    private final WebSocketMessageSender webSocketMessageSender;

    private final InProgressGamesRepo inProgressGamesRepo;

    public GameService(GamesRepo gamesRepo, PlayersRepo playersRepo , PlayersChoicesRepo playerChoiceRepo,
                       WebSocketMessageSender webSocketMessageSender,
                       InProgressGamesRepo inProgressGamesRepo){
        this.gamesRepo = gamesRepo;
        this.playersRepo = playersRepo;
        this.playerChoiceRepo = playerChoiceRepo;
        this.webSocketMessageSender = webSocketMessageSender;
        this.inProgressGamesRepo = inProgressGamesRepo;
    }


    public void startGame(GameAndConnectedPlayers game) {
        gamesRepo.save(game.getGame());
        inProgressGamesRepo.add(game);
    }

    public void playerPlayed(UUID playerId, Choice choice, UUID gameId) throws PlayerAlreadyPlayedException{
        Optional<GameAndConnectedPlayers> gameAndPlayers = getGameAndPlayers(gameId);
        if(gameAndPlayers.isEmpty()){
            webSocketMessageSender.sendToSubscribers(gameId.toString(), "No such game exists");
            return;
        }
        Optional<UUID> playerIdConnectedToGame = getPlayer(playerId, gameAndPlayers.get());
        if(playerIdConnectedToGame.isEmpty()){
            webSocketMessageSender.sendToSubscribers(gameId.toString(), "Player is not part of the game");
            return;
        }
        Optional<Player> player = playersRepo.findById(playerId);
        if(player.isEmpty()){
            return;
        }
        savePlayerChoice(choice, player.get(), gameAndPlayers.get().getGame());
    }

    private Optional<UUID> getPlayer(UUID playerId, GameAndConnectedPlayers gameAndPlayers){
        return gameAndPlayers.getPlayers().stream().filter(pid -> pid.equals(playerId))
                .findFirst();
    }

    private Optional<GameAndConnectedPlayers> getGameAndPlayers(UUID gameId) {
        InProgressGamesRepo.GameAndChoicesDTO gameAndPlayers = inProgressGamesRepo.get(gameId);
        return gameAndPlayers != null? Optional.of(gameAndPlayers.getGameAndPlayers()) : Optional.empty();
    }

    private void savePlayerChoice(Choice choice, Player player, Game game) throws PlayerAlreadyPlayedException {
        PlayerChoice playerChoice = new PlayerChoice(new PlayerChoice.GameResultId(player, game), choice);
        savePlayerChoice(game, playerChoice);
        if(hasBothPlayersChosen(game.getId())){
            concludeGame(game.getId());
        }
    }

    private void savePlayerChoice(Game game, PlayerChoice playerChoice) throws PlayerAlreadyPlayedException {
        if(hasPlayerAlreadyPlayed(game, playerChoice)){
            throw new PlayerAlreadyPlayedException();
        }

        var savedChoice = playerChoiceRepo.save(playerChoice);
        inProgressGamesRepo.saveChoice(game.getId(), savedChoice);
    }

    private boolean hasPlayerAlreadyPlayed(Game game, PlayerChoice playerChoice) {
        return inProgressGamesRepo.get(game.getId())
                .getGameChoices()
                .stream()
                .anyMatch(savedChoice -> savedChoice.getPlayer().equals(playerChoice.getPlayer()));
    }

    private boolean hasBothPlayersChosen(UUID gameId) {
        return inProgressGamesRepo.get(gameId).getGameChoices().size() == 2;
    }

    private void concludeGame(UUID gameId){
        inProgressGamesRepo.remove(gameId);
        webSocketMessageSender.sendToSubscribers(gameId.toString(), GAME_CONCLUDED);
    }
}
