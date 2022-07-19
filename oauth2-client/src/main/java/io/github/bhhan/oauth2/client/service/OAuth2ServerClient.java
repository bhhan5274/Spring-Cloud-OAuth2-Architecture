package io.github.bhhan.oauth2.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

public class OAuth2ServerClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${oauth2.server.clientId}")
    private String clientId;

    @Value("${oauth2.server.clientSecret}")
    private String clientSecret;

    @Value("${oauth2.server.url}")
    private String oauth2ServerUrl;

    public OAuth2ServerClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplateBuilder().build();
    }

    public String getUserRefreshToken(UserServiceDto.ThirdPartyUserAddReq thirdPartyUserAddReq) {
        String bearerToken = makeClientCredentialsBearerToken(clientId, clientSecret);
        UserServiceDto.UserInfo userInfo = registerThirdPartyUser(thirdPartyUserAddReq, bearerToken);
        return getRefreshToken(userInfo, getBasicAuth(clientId, clientSecret));
    }

    private String getRefreshToken(UserServiceDto.UserInfo userInfo, String basicAuth) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + basicAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", userInfo.getUsername());
        params.add("password", userInfo.getPassword());

        return getOAuth2TokenFromHttpPost(headers, params, "refresh_token");
    }

    private String makeClientCredentialsBearerToken(String clientId, String clientSecret) {
        String basicAuth = getBasicAuth(clientId, clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + basicAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");

        return getOAuth2TokenFromHttpPost(headers, params, "access_token");
    }

    private String getOAuth2TokenFromHttpPost(HttpHeaders headers, LinkedMultiValueMap<String, String> params, String property) {
        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<HashMap<String, String>> response = restTemplate.exchange(oauth2ServerUrl + "/oauth/token",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<HashMap<String, String>>() {
                });

        return Objects.requireNonNull(response.getBody()).get(property);
    }

    private String getBasicAuth(String clientId, String clientSecret) {
        String clientInfo = clientId + ":" + clientSecret;
        return new String(Base64.encodeBase64(clientInfo.getBytes()));
    }

    private UserServiceDto.UserInfo registerThirdPartyUser(UserServiceDto.ThirdPartyUserAddReq thirdPartyUserAddReq, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(thirdPartyUserAddReq), headers);
            return restTemplate.exchange(oauth2ServerUrl + "/v1/api/user",
                            HttpMethod.POST,
                            request,
                            UserServiceDto.UserInfo.class)
                    .getBody();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid information: registerThirdPartyUser");
        }
    }
}
