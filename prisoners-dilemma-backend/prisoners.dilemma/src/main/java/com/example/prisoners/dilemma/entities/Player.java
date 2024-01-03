package com.example.prisoners.dilemma.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "players")
public class Player {
    @Id
    private UUID id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id",
            foreignKey = @ForeignKey(name = "users_auth_id",
                    foreignKeyDefinition = "FOREIGN KEY (id) REFERENCES public.users_auth(id) ON DELETE CASCADE"))
    private PrisonerDilemmaAuthUser authUser;

    private String displayName;

    public Player(){
    }

    public Player(PrisonerDilemmaAuthUser authUser){
        this.authUser = authUser;
        this.id = authUser.getId();
        this.displayName = authUser.getUsername();
    }

    public static Player of(PrisonerDilemmaAuthUser authUser) {
        Player player = new Player(authUser);
        authUser.setPlayer(player);
        return player;
    }

    public PrisonerDilemmaAuthUser getId() {
        return authUser;
    }

    public String getDisplayName() {
        return displayName;
    }
}
