package com.daesoo.sso.config;

import com.daesoo.sso.member.entity.enums.ProviderType;
import com.daesoo.sso.oauth2.service.CustomOAuth2UserService;
import com.daesoo.sso.oauth2.dto.KakaoAccount;
import com.daesoo.sso.oauth2.dto.NaverAccount;
import com.daesoo.sso.oidc.service.CustomOidcUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(HttpBasicConfigurer::disable)
            .csrf(CsrfConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(configurer ->
                    configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize ->
                            authorize
                                    .requestMatchers("/actuator/**", "/swagger-ui/**", "/sign/**",
                                            "/api-docs/swagger-config", "/sign-in", "/sign-up", "/login").permitAll()
                                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll())
                .oauth2Login(oauth2Config -> oauth2Config
                        .loginPage("/login2")
                        .successHandler(successHandler())
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig
                                        .userService(customOAuth2UserService)
                                        .oidcUserService(customOidcUserService)));

        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        ObjectMapper objectMapper = new ObjectMapper();
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            String id = "";

            if(ProviderType.GOOGLE.getTitle().equals(((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId())){
                id = defaultOAuth2User.getAttributes().get("email").toString();
            }else if(ProviderType.NAVER.getTitle().equals(((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId())){
                id = objectMapper.convertValue(defaultOAuth2User.getAttributes().get("response"), NaverAccount.class).getEmail();
            }else if(ProviderType.KAKAO.getTitle().equals(((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId())){
                KakaoAccount kakaoAccount = new KakaoAccount((Map)defaultOAuth2User.getAttributes().get("kakao_account"), "id");
                id = ((String) kakaoAccount.getKakaoAccount().get("email"));
            }
            String body = """
                    {"email":"%s", "provider_type" : "%s"}
                    """.formatted(id,((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();
        });
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        return http
//                .authorizeRequests()
//                .requestMatchers("/private/**").authenticated() //private로 시작하는 uri는 로그인 필수
//                .anyRequest().permitAll() //나머지 uri는 모든 접근 허용
//                .and().oauth2Login()
//                .loginPage("/loginForm") //로그인이 필요한데 로그인을 하지 않았다면 이동할 uri 설정
//                .defaultSuccessUrl("/") //OAuth 구글 로그인이 성공하면 이동할 uri 설정
//                .userInfoEndpoint()//로그인 완료 후 회원 정보 받기
//                .userService(customOAuth2UserService).and().and().build(); //로그인 후 받아온 유저 정보 처리
//    }
}
