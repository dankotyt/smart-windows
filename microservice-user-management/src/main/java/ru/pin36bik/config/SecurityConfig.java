package ru.pin36bik.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.pin36bik.security.filter.JwtAuthFilter;
import ru.pin36bik.service.UserService;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(csrf -> csrf
                .requireCsrfProtectionMatcher(new CsrfRequestMatcher())
        )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/user/register").permitAll()
                .requestMatchers("/user/get_users").permitAll()
                .requestMatchers("/user/get_by_email/{email}").permitAll()
                .requestMatchers("/user/get_by_id/{id}").permitAll()
                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
        )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static class CsrfRequestMatcher implements RequestMatcher {
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
