/*
package com.daesoo.sso.oauth2.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Dto {
    public static String TOKEN_URI;
    public static String REDIRECT_URI;
    public static String CLIENT_ID;
    public static String CLIENTS_SECRET;

    @Value("${spring.oauth2.client.registration.google}")
    public void setTokenUri(String tokenUri){
        OAuth2Dto.TOKEN_URI = tokenUri;
    }

    @Value("${spring.oauth2.client.registration.google}")
    public void setRedirectUri(String redirectUri){
        OAuth2Dto.REDIRECT_URI = redirectUri;
    }

    @Value("${spring.oauth2.client.registration.google.client-id}")
    public void setClientId(String clientId){
        OAuth2Dto.CLIENT_ID = clientId;
    }

    @Value("${spring.oauth2.client.registration.google.client-secret}")
    public void setClientsSecret(String clientsSecret){
        OAuth2Dto.CLIENTS_SECRET = clientsSecret;
    }

    public static class OAuth2Set extends OAuth2Dto{

    }

}
*/
