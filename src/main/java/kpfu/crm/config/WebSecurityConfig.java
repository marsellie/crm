package kpfu.crm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpfu.crm.exceptions.AppExceptionHandler;
import kpfu.crm.service.UserService;
import kpfu.crm.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AppExceptionHandler resolver;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/api/login", "/login", "/js/main.js").permitAll()
                .anyRequest().authenticated()

                .and()

                .formLogin()
                .loginPage("/api/login")
                .successHandler((req, resp, auth) -> {
                    resp.setStatus(HttpStatus.OK.value());
                    resp.setContentType("application/json;charset=UTF-8");

                    HashMap<String, ?> body = ResponseFactory.ok().getBody();

                    resp.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    resp.flushBuffer();
                })
                .failureHandler((req, resp, exception) -> {
                    resp.setStatus(HttpStatus.BAD_REQUEST.value());
                    resp.setContentType("application/json;charset=UTF-8");

                    HashMap<String, ?> body = resolver.handleAll(exception).getBody();

                    resp.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    resp.flushBuffer();
                })

                .and()

                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")

                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
