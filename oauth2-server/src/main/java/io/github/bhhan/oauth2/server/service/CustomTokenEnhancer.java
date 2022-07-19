package io.github.bhhan.oauth2.server.service;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if(Objects.nonNull(authentication.getUserAuthentication())){
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("id", userPrincipal.getId());
            ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(additionalInfo);

            return accessToken;
        }

        return accessToken;
    }
}
