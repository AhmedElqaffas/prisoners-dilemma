package com.example.prisoners.dilemma.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class NotStartedGame {
    private Date createdAt;

    public NotStartedGame(Date createdAt){
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
