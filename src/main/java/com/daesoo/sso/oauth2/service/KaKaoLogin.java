package com.daesoo.sso.oauth2.service;

import com.daesoo.sso.member.entity.enums.ProviderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class KaKaoLogin implements OAuth2LoginParams{
    private String authorizationCode;

    @Override
    public String providerType() {
        return ProviderType.KAKAO.getTitle();
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}
