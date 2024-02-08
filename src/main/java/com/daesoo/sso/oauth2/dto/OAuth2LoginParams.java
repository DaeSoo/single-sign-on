package com.daesoo.sso.oauth2.dto;

import org.springframework.util.MultiValueMap;

public interface OAuth2LoginParams {
    String providerType();
    MultiValueMap<String, String> makeBody();
}
