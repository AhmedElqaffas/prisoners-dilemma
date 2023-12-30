package com.example.prisoners.dilemma.configurations;

import com.example.prisoners.dilemma.services.OAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Value("${FRONTEND_URL}")
    private String FRONTEND_URL;

    @Autowired
    private OAuth2UserService oAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(FRONTEND_URL);
        if(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken){
            saveAuthenticatedUser(oAuth2AuthenticationToken);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void saveAuthenticatedUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        oAuth2UserService.saveUser(oAuth2AuthenticationToken.getPrincipal());
    }
}
