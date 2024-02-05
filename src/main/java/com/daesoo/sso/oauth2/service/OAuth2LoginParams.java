package com.daesoo.sso.oauth2.service;

import com.daesoo.sso.member.entity.enums.ProviderType;
import org.springframework.util.MultiValueMap;

public interface OAuth2LoginParams {
    String providerType();
    MultiValueMap<String, String> makeBody();
}
