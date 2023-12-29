package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.entities.Game;
import com.example.prisoners.dilemma.repositories.AvailableGamesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;

@Service
public class ConnectionService {

    private static final String MATCH_FOUND = "MATCH_FOUND";
    private static final String PLAYER_DISCONNECTED = "PLAYER_DISCONNECTED";

    private final AvailableGamesRepo gamesRepo;

    private final WebSocketMessageSender webSocketMessageSender;
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    public ConnectionService(AvailableGamesRepo gamesRepo,
                             WebSocketMessageSender webSocketMessageSender){

        this.gamesRepo = gamesRepo;
        this.webSocketMessageSender = webSocketMessageSender;
    }

    /**
     * Connect to applicable game, or create a new one if none exists.
     */
    public Game  searchForGame(Principal user) {
        if(userAlreadyConnectedToGame(user)){
            return null;
        }
        // search for existing game to join
        Collection<Game> availableGames = gamesRepo.getAllAvailableGames();
        for(Game game: availableGames){
            return game;
        }

        // No game found, create a new game
        return gamesRepo.createNewGame();
    }

    private boolean userAlreadyConnectedToGame(Principal user) {
       return simpUserRegistry.getUsers()
               .stream()
               .map(SimpUser::getPrincipal)
               .anyMatch(principal -> principal == user);
    }

    /**
     * Called when player disconnects, this method deletes the game reference so that no player
     * gets connected to it when searching for a new game. It also notifies currently connected
     * user that other player has disconnected.
     * @param gameId
     */
    public void deleteGameAndNotifyUser(String gameId) {
        gamesRepo.deleteGame(gameId);
        webSocketMessageSender.sendToSubscribers(gameId, PLAYER_DISCONNECTED);
    }

    /**
     * Called when a player subscribes (connects) to a game. If there are now two players connected,
     * then a message is sent to the players notifying them that a match is found.
     * @param gameId
     */
    public void notifyIfMatchFound(String gameId) {
        int numberPlayersConnectedToGame = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals("/topic/game/"+gameId))
                .size();

        if(numberPlayersConnectedToGame == 2){
            // game is not available anymore, it is in progress
            gamesRepo.deleteGame(gameId);
            webSocketMessageSender.sendToSubscribers(gameId, MATCH_FOUND);
        }
    }
}
