package io.github.bhhan.oauth2.server.service;

import io.github.bhhan.oauth2.server.domain.ProviderType;
import io.github.bhhan.oauth2.server.domain.RoleType;
import io.github.bhhan.oauth2.server.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String name;
    private final String password;
    private final ProviderType providerType;
    private final RoleType roleType;
    private final Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getUserSeq(),
                user.getUsername(),
                user.getPassword(),
                user.getProviderType(),
                user.getRoleType(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRoleType().getCode()))
        );
    }
}
