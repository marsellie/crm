package kpfu.crm.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpfu.crm.exception.AppExceptionHandler;
import kpfu.crm.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class WebSecurityConfig {

    private final AppExceptionHandler resolver;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsAdapterService userDetailsAdapterService;

    @Autowired
    public void configureAuthManager(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsAdapterService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain configureHttp(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(s -> s
                        .requestMatchers("/", "/api/login", "/login", "/js/main.js").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(s -> s.loginPage("/api/login")
                        .successHandler(getAuthenticationSuccessHandler())
                        .failureHandler(getAuthenticationFailureHandler())
                )
                .logout(l -> l
                        .logoutUrl("/api/logout")
                        .logoutSuccessUrl("/")
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    private AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return (req, resp, exception) -> {
            resp.setStatus(HttpStatus.FORBIDDEN.value());
            resp.setContentType("application/json;charset=UTF-8");

            Map<String, ?> body = resolver.handle(exception).getBody();

            resp.getWriter().write(new ObjectMapper().writeValueAsString(body));
            resp.flushBuffer();
        };
    }

    private static AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (req, resp, auth) -> {
            resp.setStatus(HttpStatus.OK.value());
            resp.setContentType("application/json;charset=UTF-8");

            Map<String, ?> body = ResponseFactory.ok().getBody();

            resp.getWriter().write(new ObjectMapper().writeValueAsString(body));
            resp.flushBuffer();
        };
    }
}
