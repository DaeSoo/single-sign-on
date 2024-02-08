package com.daesoo.sso.oauth2.service;

import com.daesoo.sso.oauth2.dto.OAuthAttributes;
import com.daesoo.sso.member.entity.User;
import com.daesoo.sso.member.entity.enums.ProviderType;
import com.daesoo.sso.member.repository.UserRepository;
import com.daesoo.sso.oauth2.dto.KakaoAccount;
import com.daesoo.sso.oauth2.dto.NaverAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthAttributes oauthAttr = null;

        if(ProviderType.GOOGLE.getTitle().equals(registrationId)){
            oauthAttr = OAuthAttributes.google(userNameAttributeName, oAuth2User.getAttributes());
        }else if(ProviderType.NAVER.getTitle().equals(registrationId)){
            oauthAttr = OAuthAttributes.naver(userNameAttributeName, oAuth2User.getAttributes(), objectMapper.convertValue(oAuth2User.getAttributes().get(userNameAttributeName), NaverAccount.class));
        }else if(ProviderType.KAKAO.getTitle().equals(registrationId)){
            oauthAttr = OAuthAttributes.kakao(userNameAttributeName, oAuth2User.getAttributes(), new KakaoAccount((Map)oAuth2User.getAttributes().get("kakao_account"),userNameAttributeName));
        }else {
            return null;
        }

        User user = saveOrUpdate(oauthAttr);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(
                        user.getRoleKey())),
                oauthAttr.getAttributes(),
                oauthAttr.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
