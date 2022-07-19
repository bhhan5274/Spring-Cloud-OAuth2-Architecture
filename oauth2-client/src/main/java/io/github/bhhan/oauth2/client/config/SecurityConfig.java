package io.github.bhhan.oauth2.client.config;

import io.github.bhhan.oauth2.client.service.ClientRedirectHandler;
import io.github.bhhan.oauth2.client.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${oauth2.server.redirectUrl}")
    private String redirectUrl;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomOAuth2ClientConfigurer customOAuth2ClientConfigurer = new CustomOAuth2ClientConfigurer(
                customOAuth2UserService,
                new ClientRedirectHandler(redirectUrl)
        );

        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .apply(customOAuth2ClientConfigurer);
    }
}
