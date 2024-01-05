package com.example.prisoners.dilemma.dtos;

public class PlayerGameResultDTO {

    private Choice playerChoice;
    private Choice otherPlayerChoice;
    private int moneyGained;

    /**
     * Default constructor and getters are added for {@link org.springframework.messaging.converter.MappingJackson2MessageConverter}
     * used when sending message using {@link org.springframework.messaging.simp.SimpMessagingTemplate}
     */
    public PlayerGameResultDTO(){}

    public Choice getPlayerChoice() {
        return playerChoice;
    }


    public Choice getOtherPlayerChoice() {
        return otherPlayerChoice;
    }


    public int getMoneyGained() {
        return moneyGained;
    }

    public PlayerGameResultDTO(Choice playerChoice,
                               Choice otherPlayerChoice,
                               int moneyGained){
        this.playerChoice = playerChoice;
        this.otherPlayerChoice = otherPlayerChoice;
        this.moneyGained = moneyGained;
    }
}
