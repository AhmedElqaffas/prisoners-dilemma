package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.dtos.GameAndConnectedPlayers;
import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.entities.Player;
import com.example.prisoners.dilemma.repositories.AvailableGamesRepo;
import com.example.prisoners.dilemma.repositories.PlayersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConnectionService {

    private static final String MATCH_FOUND = "MATCH_FOUND";
    private static final String PLAYER_DISCONNECTED = "PLAYER_DISCONNECTED";

    private final AvailableGamesRepo availableGamesRepo;
    private final GameService gameService;

    private final WebSocketMessageSender webSocketMessageSender;

    private final PlayersRepo playersRepo;
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    public ConnectionService(AvailableGamesRepo gamesRepo,
                             GameService gameService,
                             WebSocketMessageSender webSocketMessageSender,
                             PlayersRepo playersRepo){

        this.availableGamesRepo = gamesRepo;
        this.gameService = gameService;
        this.webSocketMessageSender = webSocketMessageSender;
        this.playersRepo = playersRepo;
    }

    /**
     * Finds the id of an applicable game to connect to, or return an id for a new game.
     */
    public UUID  searchForGame(Principal user) {
        if(userAlreadyConnectedToGame(user)){
            return null;
        }
        // search for existing game to join
        Collection<Game> availableGames = availableGamesRepo.getAllAvailableGames()
                .stream()
                .map(GameAndConnectedPlayers::getGame)
                .toList();
        for(Game game: availableGames){
            return game.getId();
        }

        // No game found, create a new game
        return UUID.randomUUID();
    }

    private boolean userAlreadyConnectedToGame(Principal user) {
       return simpUserRegistry.getUsers()
               .stream()
               .map(SimpUser::getPrincipal)
               .anyMatch(principal -> user.getName().equals(principal.getName()));
    }

    /**
     * Called when player disconnects, this method deletes the game reference so that no player
     * gets connected to it when searching for a new game. It also notifies currently connected
     * user that other player has disconnected.
     * @param gameId
     */
    public void deleteGameAndNotifyUser(UUID gameId) {
        availableGamesRepo.deleteGame(gameId);
        webSocketMessageSender.sendToSubscribers(gameId.toString(), PLAYER_DISCONNECTED);
    }

    /**
     * Called when a player subscribes (connects) to a game. If there are now two players connected,
     * then a message is sent to the players notifying them that a match is found.
     * If this is the first player to connect, create a new game.
     * @param gameId
     */
    public void playerConnectedToGame(UUID gameId, UUID playerId) {
        int numberPlayersConnectedToGame = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals("/topic/game/"+gameId))
                .size();

        if(numberPlayersConnectedToGame == 1){
            createGame(gameId, playerId);
        } else if(numberPlayersConnectedToGame == 2){
            startGame(gameId, playerId);
        }
    }

    private void createGame(UUID gameId, UUID playerId) {
        Optional<Player> creator = playersRepo.findById(playerId);
        if(creator.isEmpty()){
            return;
        }
        availableGamesRepo.createNewGame(new Game(gameId), creator.get());
    }

    private void startGame(UUID gameId, UUID secondPlayerId) {
        Optional<GameAndConnectedPlayers> gameAndPlayers = availableGamesRepo.getGameAndPlayers(gameId);
        if(gameAndPlayers.isEmpty()){
            // if no game was found, then the other player has already disconnected
            webSocketMessageSender.sendToSubscribers(gameId.toString(), PLAYER_DISCONNECTED);
            return;
        }

        Optional<Player> secondPlayer = playersRepo.findById(secondPlayerId);
        if(secondPlayer.isEmpty()){
            return;
        }

        gameAndPlayers.get().addPlayer(secondPlayer.get());
        // game is not available anymore, it is in progress
        availableGamesRepo.deleteGame(gameId);
        gameService.startGame(gameAndPlayers.get());
        webSocketMessageSender.sendToSubscribers(gameId.toString(), MATCH_FOUND);
    }
}
