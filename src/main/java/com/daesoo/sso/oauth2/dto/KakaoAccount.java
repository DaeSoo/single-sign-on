package com.daesoo.sso.oauth2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class KakaoAccount {
    private String id;

    private Map<String, Object> kakaoAccount;

    public KakaoAccount(Map<String, Object> attributes, String id ) {
        this.kakaoAccount = attributes;
        this.id = id;
    }
}
