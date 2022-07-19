package io.github.bhhan.oauth2.server.service;

import io.github.bhhan.oauth2.server.domain.ProviderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserServiceDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ThirdPartyUserAddReq {
        @NotNull @Size(max = 64)
        private String userId;
        @NotNull @Size(max = 100)
        private String name;
        @NotNull @Size(max = 512)
        private String email;
        @NotNull @Size(max = 100)
        private String imageUrl;
        @NotNull
        private ProviderType providerType;

        public ThirdPartyUserAddReq(String userId, String name, String email, String imageUrl, ProviderType providerType) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.imageUrl = imageUrl;
            this.providerType = providerType;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserInfo {
        private String username;
        private String password;

        public UserInfo(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
