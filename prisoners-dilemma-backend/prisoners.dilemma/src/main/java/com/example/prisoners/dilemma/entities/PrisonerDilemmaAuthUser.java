package com.example.prisoners.dilemma.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table(name = "users_auth")
public class PrisonerDilemmaAuthUser implements UserDetails{
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(mappedBy = "authUser")
    @PrimaryKeyJoinColumn
    private Player player;

    private String username;
    private String password;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked  = true;
    private boolean isCredentialsNonExpired  = true;
    private boolean isEnabled  = true;

    private String picture;

    /**
     * Whether this user is an internal user or from OAuth2
     */
    private boolean isExternal;

    public PrisonerDilemmaAuthUser(){

    }

    private PrisonerDilemmaAuthUser(String username, String picture){
        this.username = username;
        this.picture = picture;
        this.isExternal = true;
        this.id = UUID.randomUUID();
    }

    public static PrisonerDilemmaAuthUser newExternalUser(String username, String picture){
        return new PrisonerDilemmaAuthUser(username, picture);
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
