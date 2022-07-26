package io.github.bhhan.oauth2.client.provider.info;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {
    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("login");
    }

    @Override
    public String getEmail() {
        return attributes.get("email") == null ? "empty" : (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
