package com.example.prisoners.dilemma.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {

    @Id
    private final UUID id;
    private final Date createdAt;

    public Game(){
        this.id = UUID.randomUUID();
        this.createdAt = new Date(System.currentTimeMillis());
    }

    public Game(UUID id){
        this.id = id;
        this.createdAt = new Date(System.currentTimeMillis());
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UUID getId() {
        return id;
    }
}
