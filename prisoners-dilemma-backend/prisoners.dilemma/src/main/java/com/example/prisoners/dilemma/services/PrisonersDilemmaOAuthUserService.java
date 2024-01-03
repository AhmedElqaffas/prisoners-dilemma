package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.dtos.OAuth2UserWithId;
import com.example.prisoners.dilemma.entities.PrisonerDilemmaAuthUser;
import com.example.prisoners.dilemma.repositories.UserAuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PrisonersDilemmaOAuthUserService extends DefaultOAuth2UserService {

    @Autowired
    private UserAuthRepo userRepo;
    @Autowired
    private PlayerService playerService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        PrisonerDilemmaAuthUser newUser = PrisonerDilemmaAuthUser.newExternalUser(
                oAuth2User.getAttributes().get("email").toString(),
                oAuth2User.getAttributes().get("picture").toString()
        );

        String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2UserWithId oAuth2UserWithId =  new OAuth2UserWithId(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), nameAttributeKey);

        Optional<PrisonerDilemmaAuthUser> existingUser = userRepo.findByUsername(oAuth2User.getAttributes().get("email").toString());
        if(existingUser.isPresent()){
            updateExistingUser(existingUser.get(), newUser);
            oAuth2UserWithId.setId(existingUser.get().getId());
        } else {
            registerNewUser(newUser);
            oAuth2UserWithId.setId(newUser.getId());
            createNewPlayer(newUser);
        }
        return oAuth2UserWithId;
    }

    private PrisonerDilemmaAuthUser updateExistingUser(PrisonerDilemmaAuthUser existingUser, PrisonerDilemmaAuthUser updatedUser) {
        existingUser.setPicture(updatedUser.getPicture());
        return userRepo.save(existingUser);
    }

    private PrisonerDilemmaAuthUser registerNewUser(PrisonerDilemmaAuthUser newUser) {
        return userRepo.save(newUser);
    }

    private void createNewPlayer(PrisonerDilemmaAuthUser authUser) {
        playerService.createPlayer(authUser);
    }

}
