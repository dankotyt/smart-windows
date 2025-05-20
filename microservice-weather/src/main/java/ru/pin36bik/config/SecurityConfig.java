package ru.pin36bik.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/weather/**")
                        .access((authentication, context) -> {
                            String validToken = context.getRequest().getHeader("X-Valid-Token");
                            String weatherKey = context.getRequest().getHeader("X-Yandex-Weather-Key");
                            return new AuthorizationDecision(
                                    "true".equals(validToken) &&
                                            weatherKey != null &&
                                            !weatherKey.isEmpty()
                            );
                        })
                        .anyRequest().denyAll()
                );
        return http.build();
    }
}
