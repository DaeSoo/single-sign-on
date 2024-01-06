package com.daesoo.sso.member.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {
    KEYCLOAK("KEYCLOAK", "keycloak"),
    NAVER("NAVER", "naver"),
    KAKAO("KAKAO", "kakao"),
    GOOGLE("GOOGLE", "google");

    private final String key;
    private final String title;
}
