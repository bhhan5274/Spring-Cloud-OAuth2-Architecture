package io.github.bhhan.oauth2.client.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class CustomOAuth2ClientConfigurer extends AbstractHttpConfigurer<CustomOAuth2ClientConfigurer, HttpSecurity> {
    private final String loginPageUrl = "/login";
    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    public CustomOAuth2ClientConfigurer(DefaultOAuth2UserService defaultOAuth2UserService, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.defaultOAuth2UserService = defaultOAuth2UserService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(loginPageUrl)))
                .oauth2Login(oauth2Login -> {
                    oauth2Login.userInfoEndpoint()
                            .userService(defaultOAuth2UserService);
                    oauth2Login.successHandler(authenticationSuccessHandler);
                });
    }
}
