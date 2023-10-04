package kpfu.crm.config.security;

import kpfu.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsAdapterService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetailImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findByUsername(username)
                .map(UserDetailImpl::new)
                .orElseThrow(() -> {
                    String errorMessage = "Not found by username = %s".formatted(username);
                    return new UsernameNotFoundException(errorMessage);
                });
    }
}
