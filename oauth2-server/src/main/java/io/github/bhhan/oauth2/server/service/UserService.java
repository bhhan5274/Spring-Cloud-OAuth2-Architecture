package io.github.bhhan.oauth2.server.service;

import io.github.bhhan.oauth2.server.domain.ProviderType;
import io.github.bhhan.oauth2.server.domain.RoleType;
import io.github.bhhan.oauth2.server.domain.User;
import io.github.bhhan.oauth2.server.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserId is invalid: " + userId));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email is invalid: " + email));
    }

    @Transactional
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getRawPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUserIdAndProviderType(String userId, ProviderType providerType) {
        return userRepository.findByUserIdAndProviderType(userId, providerType);
    }

    @Transactional
    public UserServiceDto.UserInfo addThirdPartyUser(UserServiceDto.ThirdPartyUserAddReq req) {
        User user = new User(
                req.getUserId(),
                req.getEmail(),
                Strings.isNotBlank(req.getEmail()) ? "y" : "n",
                req.getImageUrl(),
                req.getProviderType(),
                RoleType.USER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        user.setPassword(passwordEncoder.encode(user.getRawPassword()));
        User savedUser = userRepository.save(user);
        String username = savedUser.getUserSeq() + "번님";
        savedUser.setUsername(username);

        return new UserServiceDto.UserInfo(username, savedUser.getRawPassword());
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is invalid: " + username));

        return UserPrincipal.create(user);
    }
}
