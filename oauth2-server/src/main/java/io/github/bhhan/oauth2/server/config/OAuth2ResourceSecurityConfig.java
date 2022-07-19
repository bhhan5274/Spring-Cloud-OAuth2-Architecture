package io.github.bhhan.oauth2.server.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

public class OAuth2ResourceSecurityConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                /*.antMatchers("/v1/**").hasRole("ADMIN")*/
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .httpBasic()
                .and()
                .formLogin();
    }
}
