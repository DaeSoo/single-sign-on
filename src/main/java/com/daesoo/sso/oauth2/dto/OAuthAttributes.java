package com.daesoo.sso.oauth2.dto;

import com.daesoo.sso.member.entity.User;
import com.daesoo.sso.member.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes google(String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes naver(String userNameAttributeName, Map<String, Object> attributes, NaverAccount naverAccount) {
        return ofNaver(userNameAttributeName, attributes, naverAccount);
    }

    public static OAuthAttributes kakao(String userNameAttributeName, Map<String, Object> attributes, KakaoAccount kakaoAccount) {
        return ofKaKao(userNameAttributeName, attributes, kakaoAccount);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes, NaverAccount naverAccount) {
        return OAuthAttributes.builder()
                .name(naverAccount.getName())
                .email(naverAccount.getEmail())
//                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    private static OAuthAttributes ofKaKao(String userNameAttributeName, Map<String, Object> attributes, KakaoAccount kakaoAccount) {
        return OAuthAttributes.builder()
                .email((String) kakaoAccount.getKakaoAccount().get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .role(Role.GUEST)
                .build();
    }
}
