package com.example.prisoners.dilemma.exceptions;

public class PlayerAlreadyPlayedException extends Exception{
    public PlayerAlreadyPlayedException(){
        super("Player has already made a choice");
    }
}
