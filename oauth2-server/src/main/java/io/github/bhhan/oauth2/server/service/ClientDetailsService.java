package io.github.bhhan.oauth2.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

@RequiredArgsConstructor
public class ClientDetailsService {
    private final JdbcClientDetailsService clientDetailsService;

    public void addClientDetails(ClientDetails clientDetails) {
        clientDetailsService.addClientDetails(clientDetails);
    }

    public void updateClientDetails(ClientDetails clientDetails) {
        clientDetailsService.updateClientDetails(clientDetails);
    }

    public void updateClientSecret(String clientId, String secret) {
        clientDetailsService.updateClientSecret(clientId, secret);
    }

    public void removeClientDetails(String clientId) {
        clientDetailsService.removeClientDetails(clientId);
    }
}
