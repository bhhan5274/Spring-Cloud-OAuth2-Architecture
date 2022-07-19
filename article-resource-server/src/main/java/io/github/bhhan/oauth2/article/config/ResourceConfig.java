package io.github.bhhan.oauth2.article.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableWebSecurity
public class ResourceConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new ResourceRoleConverter());

        http.authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/v1/articles/**")
                .hasAuthority("SCOPE_read")
                .antMatchers(HttpMethod.POST, "/v1/articles/**")
                .hasAuthority("SCOPE_write")
                .antMatchers(HttpMethod.DELETE, "/v1/articles/**")
                .hasAuthority("SCOPE_delete")
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter);
    }
}
