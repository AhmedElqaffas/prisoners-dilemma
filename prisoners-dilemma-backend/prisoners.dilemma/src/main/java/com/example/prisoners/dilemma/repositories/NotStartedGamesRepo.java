package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.configurations.RabbitMQConfiguration;
import com.example.prisoners.dilemma.entities.NotStartedGame;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class NotStartedGamesRepo implements Serializable {

    private final RabbitTemplate rabbitTemplate;

    public NotStartedGamesRepo(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void createNewGame() {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.NOT_STARTED_GAMES_QUEUE, new NotStartedGame(new Date(System.currentTimeMillis())));
    }
}
