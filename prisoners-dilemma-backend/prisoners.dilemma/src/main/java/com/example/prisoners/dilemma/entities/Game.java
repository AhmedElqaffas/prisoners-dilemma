package com.example.prisoners.dilemma.entities;

import java.util.Date;
import java.util.UUID;

public class Game {
    private final UUID id;
    private final Date createdAt;

    public Game(){
        this.id = UUID.randomUUID();
        this.createdAt = new Date(System.currentTimeMillis());
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UUID getId() {
        return id;
    }
}
