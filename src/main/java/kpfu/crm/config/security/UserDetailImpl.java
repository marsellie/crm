package kpfu.crm.config.security;

import kpfu.crm.model.user.AppUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Objects;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class UserDetailImpl extends User {

    private final UUID id;
    private final String name;

    public UserDetailImpl(AppUser appUser) {
        super(
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getRoles().stream()
                        .map(Objects::toString)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );

        id = appUser.getId();
        name = appUser.getName();
    }
}
