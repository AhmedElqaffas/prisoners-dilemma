package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.entities.PrisonerDilemmaAuthUser;
import com.example.prisoners.dilemma.repositories.UserAuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuth2UserService {

    @Autowired
    private UserAuthRepo userRepo;

    public void saveUser(OAuth2User user) {
        PrisonerDilemmaAuthUser newUser = PrisonerDilemmaAuthUser.newExternalUser(
                user.getAttributes().get("email").toString(),
                user.getAttributes().get("email").toString(),
                user.getAttributes().get("picture").toString()
        );

        Optional<PrisonerDilemmaAuthUser> existingUser = userRepo.findByEmail(newUser.getEmail());

        if(existingUser.isPresent()){
            updateExistingUser(existingUser.get(), newUser);
        } else {
            registerNewUser(newUser);
        }
    }

    private void updateExistingUser(PrisonerDilemmaAuthUser existingUser, PrisonerDilemmaAuthUser updatedUser) {
        existingUser.setPicture(updatedUser.getPicture());
        userRepo.save(existingUser);
    }

    private void registerNewUser(PrisonerDilemmaAuthUser newUser) {
        userRepo.save(newUser);
    }
}
