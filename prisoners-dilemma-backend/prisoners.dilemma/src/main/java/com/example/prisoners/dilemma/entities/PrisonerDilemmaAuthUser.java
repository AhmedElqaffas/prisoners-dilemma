package com.example.prisoners.dilemma.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String username;
    private String password;

    private String email;

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

    private PrisonerDilemmaAuthUser(String username, String email, String picture){
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.isExternal = true;
    }

    public static PrisonerDilemmaAuthUser newExternalUser(String username, String email, String picture){
        return  new PrisonerDilemmaAuthUser(username, email, picture);
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

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }
}
