package io.github.bhhan.oauth2.client;

import io.github.bhhan.oauth2.client.config.ResourceConfig;
import io.github.bhhan.oauth2.client.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * TODO
 * 1. Signing Key replace Private / Public Key
 */


@SpringBootApplication
@EnableWebSecurity
@Import({ResourceConfig.class, SecurityConfig.class})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
