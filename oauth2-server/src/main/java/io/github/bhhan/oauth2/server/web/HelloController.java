package io.github.bhhan.oauth2.server.web;

import io.github.bhhan.oauth2.server.domain.User;
import io.github.bhhan.oauth2.server.service.UserService;
import io.github.bhhan.oauth2.server.service.UserServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class HelloController {
    private final UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @PreAuthorize("#oauth2.hasScope('write') or hasRole('ADMIN')")
    @PostMapping("/user")
    public UserServiceDto.UserInfo addThirdPartyUser(@Valid @RequestBody UserServiceDto.ThirdPartyUserAddReq req) {
        Optional<User> findUser = userService.findUserByUserIdAndProviderType(req.getUserId(), req.getProviderType());

        return findUser.map(user -> new UserServiceDto.UserInfo(user.getUsername(), user.getRawPassword()))
                .orElseGet(() -> userService.addThirdPartyUser(req));
    }
}
