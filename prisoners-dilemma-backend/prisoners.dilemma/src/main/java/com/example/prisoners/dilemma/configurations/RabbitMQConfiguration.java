package com.example.prisoners.dilemma.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {

    public static final String NOT_STARTED_GAMES_QUEUE = "notStartedGamesQueue";
    public static final String NOT_STARTED_GAMES_EXCHANGE = "notStartedGamesExchange";
    public static final String DEAD_LETTER_EXCHANGE_NAME = "deadLetterExchange";
    public static final String DEAD_LETTER_QUEUE_NAME = "deadLetterQueue";

    public static final int TTL_MILLI_SECONDS = 60000;

    @Bean
    public Binding deadLetterQueueBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_QUEUE_NAME);
    }

    @Bean
    public Binding notStartedGamesQueueBinding() {
        return BindingBuilder.bind(notStartedGamesQueue())
                .to(notStartedGamesExchange())
                .with(NOT_STARTED_GAMES_QUEUE);
    }

    @Bean
    public Queue notStartedGamesQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_NAME);
        args.put("x-message-ttl", TTL_MILLI_SECONDS);

        return new Queue(NOT_STARTED_GAMES_QUEUE, false, false, false, args);
    }

    @Bean
    public DirectExchange notStartedGamesExchange() {
        return new DirectExchange(NOT_STARTED_GAMES_EXCHANGE);
    }
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE_NAME, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE_NAME);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        rabbitAdmin.declareQueue(deadLetterQueue());
        rabbitAdmin.declareExchange(deadLetterExchange());
        rabbitAdmin.declareBinding(deadLetterQueueBinding());


        rabbitAdmin.declareQueue(notStartedGamesQueue());
        rabbitAdmin.declareExchange(notStartedGamesExchange());
        rabbitAdmin.declareBinding(notStartedGamesQueueBinding());



        return rabbitAdmin;
    }
}
