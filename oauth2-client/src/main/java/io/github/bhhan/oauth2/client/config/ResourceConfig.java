package io.github.bhhan.oauth2.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bhhan.oauth2.client.service.CustomOAuth2UserService;
import io.github.bhhan.oauth2.client.service.OAuth2ServerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class ResourceConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public CustomOAuth2UserService customOAuth2UserService(OAuth2ServerClient oAuth2ServerClient) {
        return new CustomOAuth2UserService(oAuth2ServerClient);
    }

    @Bean
    public OAuth2ServerClient oAuth2ServerClient() {
        return new OAuth2ServerClient(objectMapper);
    }
}
