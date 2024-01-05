package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.dtos.Choice;
import com.example.prisoners.dilemma.dtos.GameAndConnectedPlayers;
import com.example.prisoners.dilemma.dtos.PlayerGameResultDTO;
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
import java.util.function.Predicate;

@Service
public class GameService {
    private final GamesRepo gamesRepo;
    private final PlayersRepo playersRepo;
    private final PlayersChoicesRepo playerChoiceRepo;
    private final WebSocketMessageSender webSocketMessageSender;

    private final InProgressGamesRepo inProgressGamesRepo;

    private final PlayerService playerService;

    public GameService(GamesRepo gamesRepo, PlayersRepo playersRepo , PlayersChoicesRepo playerChoiceRepo,
                       WebSocketMessageSender webSocketMessageSender,
                       InProgressGamesRepo inProgressGamesRepo,
                       PlayerService playerService){
        this.gamesRepo = gamesRepo;
        this.playersRepo = playersRepo;
        this.playerChoiceRepo = playerChoiceRepo;
        this.webSocketMessageSender = webSocketMessageSender;
        this.inProgressGamesRepo = inProgressGamesRepo;
        this.playerService = playerService;
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
        Optional<Player> playerIdConnectedToGame = getPlayer(playerId, gameAndPlayers.get());
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

    private Optional<Player> getPlayer(UUID playerId, GameAndConnectedPlayers gameAndPlayers){
        return gameAndPlayers.getPlayers().stream().filter(player -> player.getId().equals(playerId))
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
        var gameAndChoices = inProgressGamesRepo.get(gameId);
        inProgressGamesRepo.remove(gameId);
        for(Player player: gameAndChoices.getGameAndPlayers().getPlayers()){
            PlayerGameResultDTO playerGameResult = getPlayerGameResult(gameAndChoices, player);
            playerService.updatePlayerWealth(player, playerGameResult, gameAndChoices.getGameAndPlayers().getGame());
            webSocketMessageSender.sendToUser(player.getId().toString(), playerGameResult);
        }

    }
    private PlayerGameResultDTO getPlayerGameResult(InProgressGamesRepo.GameAndChoicesDTO gameAndChoices, Player player) {
        Predicate<PlayerChoice> filterToGetCurrentPlayerChoice = pc -> pc.getPlayer().equals(player);
        PlayerChoice playerChoice = getPlayerChoice(gameAndChoices, filterToGetCurrentPlayerChoice);
        PlayerChoice otherPlayerChoice = getPlayerChoice(gameAndChoices, filterToGetCurrentPlayerChoice.negate());
        return new PlayerGameResultDTO(playerChoice.getChoice(), otherPlayerChoice.getChoice(), calculateMoneyGained(playerChoice, otherPlayerChoice));
    }

    private PlayerChoice getPlayerChoice(InProgressGamesRepo.GameAndChoicesDTO gameAndChoices, Predicate<PlayerChoice> playerChoiceFilter) {
        return gameAndChoices.getGameChoices()
                .stream()
                .filter(playerChoiceFilter)
                .findFirst()
                .get();
    }

    private int calculateMoneyGained(PlayerChoice playerChoice, PlayerChoice otherPlayerChoice) {
        if(playerChoice.getChoice().equals(Choice.SPLIT)
                && otherPlayerChoice.getChoice().equals(Choice.SPLIT)){
            return 500;
        } else if(playerChoice.getChoice().equals(Choice.KEEP)
            && otherPlayerChoice.getChoice().equals(Choice.SPLIT)){
            return (int) - (0.5 * playerChoice.getPlayer().getWealth());
        } else if (playerChoice.getChoice().equals(Choice.SPLIT)
                && otherPlayerChoice.getChoice().equals(Choice.KEEP)) {
            return (int) (0.5 * otherPlayerChoice.getPlayer().getWealth());
        } else{
            return 0;
        }
    }
}
