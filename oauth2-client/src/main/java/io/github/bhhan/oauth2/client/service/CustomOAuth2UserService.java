package io.github.bhhan.oauth2.client.service;

import io.github.bhhan.oauth2.client.provider.ProviderType;
import io.github.bhhan.oauth2.client.provider.info.OAuth2UserInfo;
import io.github.bhhan.oauth2.client.provider.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final OAuth2ServerClient oAuth2ServerClient;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());

        UserServiceDto.ThirdPartyUserAddReq thirdPartyUserAddReq = new UserServiceDto.ThirdPartyUserAddReq(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                userInfo.getImageUrl(),
                providerType
        );

        String userRefreshToken = oAuth2ServerClient.getUserRefreshToken(thirdPartyUserAddReq);

        return new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return userInfo.getAttributes();
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptyList();
            }

            @Override
            public String getName() {
                return userRefreshToken;
            }
        };
    }
}
