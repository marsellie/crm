package kpfu.crm.config;

import jakarta.annotation.PostConstruct;
import kpfu.crm.model.user.AppUser;
import kpfu.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static kpfu.crm.model.user.Role.ADMIN;
import static kpfu.crm.model.user.Role.USER;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StartupConfig {

    private final AppConfig appConfig;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        String username = appConfig.getUsername();
        String password = appConfig.getPassword();


        log.info("""
                
                
                                
                ---ADMIN USERNAME, PASSWORD: < {} , {} >---
                
                
                """, username, password);

        AppUser appUser = userService.findByUsername(username)
                .orElseGet(AppUser::new);

        appUser.setUsername(username);
        appUser.setName("Администратор");
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.setRoles(Set.of(ADMIN, USER));

        userService.save(appUser);
    }
}
