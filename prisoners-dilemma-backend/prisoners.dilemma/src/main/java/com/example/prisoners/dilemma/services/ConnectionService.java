package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.configurations.RabbitMQConfiguration;
import com.example.prisoners.dilemma.repositories.NotStartedGamesRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {

    private final NotStartedGamesRepo gamesRepo;

    public ConnectionService(NotStartedGamesRepo gamesRepo){
        this.gamesRepo = gamesRepo;
    }

    /**
     * Connect to applicable game, or create a new one if none exists.
     */
    public void searchForGame(String userConnectionUUID) {
        // No game found, create a new game
        gamesRepo.createNewGame();
    }

    @RabbitListener(queues = RabbitMQConfiguration.DEAD_LETTER_QUEUE_NAME)
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);
    }
}
