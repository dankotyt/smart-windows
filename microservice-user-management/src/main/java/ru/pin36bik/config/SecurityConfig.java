package ru.pin36bik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .requireCsrfProtectionMatcher(new CsrfRequestMatcher())
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/register").permitAll()
                .requestMatchers("/user/get_users").permitAll()
                .requestMatchers("/user/get_by_email/{email}").permitAll()
                .requestMatchers("/user/get_by_id/{id}").permitAll()
                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
        );

        return http.build();
    }



    private static class CsrfRequestMatcher implements RequestMatcher {
        // Список исключенных эндпоинтов
        private final List<AntPathRequestMatcher> excludedMatchers = Arrays.asList(
                new AntPathRequestMatcher("/user/register", "POST"),
                new AntPathRequestMatcher("/user/get_users", "GET"),
                new AntPathRequestMatcher("/user/get_by_email/{email}", "GET"),
                new AntPathRequestMatcher("/user/get_by_id/{id}", "GET")
        );

        @Override
        public boolean matches(HttpServletRequest request) {
            // Если запрос совпадает с одним из исключенных эндпоинтов, CSRF не требуется
            return excludedMatchers.stream()
                    .noneMatch(matcher -> matcher.matches(request));
        }
    }

}
