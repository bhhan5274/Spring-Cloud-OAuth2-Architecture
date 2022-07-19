package io.github.bhhan.oauth2.server;

import io.github.bhhan.oauth2.server.config.OAuth2Config;
import io.github.bhhan.oauth2.server.config.OAuth2ResourceConfig;
import io.github.bhhan.oauth2.server.config.OAuth2ResourceSecurityConfig;
import io.github.bhhan.oauth2.server.config.OAuth2SecurityConfig;
import io.github.bhhan.oauth2.server.domain.ProviderType;
import io.github.bhhan.oauth2.server.domain.RoleType;
import io.github.bhhan.oauth2.server.domain.User;
import io.github.bhhan.oauth2.server.service.ClientDetailsService;
import io.github.bhhan.oauth2.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({OAuth2ResourceConfig.class, OAuth2Config.class, OAuth2SecurityConfig.class, OAuth2ResourceSecurityConfig.class})
@RequiredArgsConstructor
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner initOAuth2(ClientDetailsService clientDetailsService, UserService userService) {
        return (args) -> {
            User admin = new User(
                    "bhhan",
                    "bhhan",
                    "hbh5274@gmail.com",
                    "y",
                    "",
                    ProviderType.NONE,
                    RoleType.ADMIN,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            User user = new User(
                    "user",
                    "user",
                    "user@gmail.com",
                    "y",
                    "",
                    ProviderType.KAKAO,
                    RoleType.USER,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            userService.addUser(admin);
            userService.addUser(user);

            BaseClientDetails client1 = new BaseClientDetails(
                    "client1",
                    "",
                    "read,delete",
                    "authorization_code,refresh_token,password,client_credentials",
                    "",
                    "http://localhost:8081/oauth2/callback"
            );
            client1.setClientSecret("secret");

            BaseClientDetails client2 = new BaseClientDetails(
                    "client2",
                    "",
                    "read,write",
                    "authorization_code,refresh_token,password,client_credentials",
                    "",
                    "http://localhost:8081/oauth2/callback"
            );
            client2.setClientSecret("secret");

            clientDetailsService.addClientDetails(client1);
            clientDetailsService.addClientDetails(client2);
        };
    }
}
