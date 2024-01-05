package com.example.prisoners.dilemma.services;

import com.example.prisoners.dilemma.entities.PrisonerDilemmaAuthUser;
import com.example.prisoners.dilemma.repositories.UserAuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Component
public class PrisonersDilemmaOAuthUserService extends DefaultOAuth2UserService {

    private static final String NAME_ATTRIBUTE_KEY = "customName";
    @Autowired
    private UserAuthRepo userRepo;
    @Autowired
    private PlayerService playerService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        PrisonerDilemmaAuthUser newUser = PrisonerDilemmaAuthUser.newExternalUser(
                oAuth2User.getAttributes().get("email").toString(),
                oAuth2User.getAttributes().get("picture").toString()
        );
        OAuth2User oAuth2UserWithId;
        Optional<PrisonerDilemmaAuthUser> existingUser = userRepo.findByUsername(oAuth2User.getAttributes().get("email").toString());
        if(existingUser.isPresent()){
            oAuth2UserWithId = createNewOAuth2User(oAuth2User, existingUser.get().getId());
            updateExistingUser(existingUser.get(), newUser);
        } else {
            oAuth2UserWithId = createNewOAuth2User(oAuth2User, newUser.getId());
            registerNewUser(newUser);
            createNewPlayer(newUser);
        }
        return oAuth2UserWithId;
    }

    private PrisonerDilemmaAuthUser updateExistingUser(PrisonerDilemmaAuthUser existingUser,
                                                 PrisonerDilemmaAuthUser updatedUser) {
        existingUser.setPicture(updatedUser.getPicture());
        return userRepo.save(existingUser);
    }

    private PrisonerDilemmaAuthUser registerNewUser(PrisonerDilemmaAuthUser newUser) {
        return userRepo.save(newUser);
    }

    private OAuth2User createNewOAuth2User(OAuth2User oldUser, UUID userId){
        var modifiableAttributesMap = new HashMap<>(oldUser.getAttributes());
        modifiableAttributesMap.put(NAME_ATTRIBUTE_KEY, userId);
        return new DefaultOAuth2User(oldUser.getAuthorities(), modifiableAttributesMap , NAME_ATTRIBUTE_KEY);
    }

    private void createNewPlayer(PrisonerDilemmaAuthUser authUser) {
        playerService.createPlayer(authUser);
    }

}
