package ru.pin36bik.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.pin36bik.security.filter.JwtAuthFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http
                // CORS всегда включен (и для dev, и для production)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF настройка (для dev оставляем текущую, для production раскомментировать нижнюю)

                .csrf(csrf -> csrf
                        .requireCsrfProtectionMatcher(new CsrfRequestMatcher())
                )

                //.csrf(csrf -> csrf.disable())

                /* Для production вместо верхнего блока:
                .csrf(csrf -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/api/v1/auth/register", "/api/v1/auth/login")
                )
                */
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                        // Для production убрать public доступ к user endpoints
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/refresh").authenticated()
                        .requestMatchers("/api/v1/auth/logout").authenticated()
                        .requestMatchers("/user/get_users").permitAll()          // Удалить в production
                        .requestMatchers("/user/get_by_id/{id}").permitAll()     // Удалить в production
                        .requestMatchers("/user/get_by_email/{email}").permitAll() // Удалить в production
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider);

        return http.build();
    }

    // Для production можно удалить этот класс полностью
    private static class CsrfRequestMatcher implements RequestMatcher {
        private final List<AntPathRequestMatcher> excludedMatchers = Arrays.asList(
                new AntPathRequestMatcher("/api/v1/auth/register", "POST"),
                new AntPathRequestMatcher("/api/v1/auth/login", "POST"),
                new AntPathRequestMatcher("/api/v1/auth/refresh", "POST"),
                new AntPathRequestMatcher("/api/v1/auth/logout", "POST"),
                new AntPathRequestMatcher("/user/get_users", "GET"),          // Удалить в production
                new AntPathRequestMatcher("/user/get_by_email/{email}", "GET"), // Удалить в production
                new AntPathRequestMatcher("/user/get_by_id/{id}", "GET")      // Удалить в production
        );

        @Override
        public boolean matches(HttpServletRequest request) {
            return excludedMatchers.stream()
                    .noneMatch(matcher -> matcher.matches(request));
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Для production заменить на конкретные домены
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",  // Dev фронтенд
                "https://your-production-domain.com" // Production фронтенд
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        // Для production добавить:
        // config.setExposedHeaders(List.of("X-XSRF-TOKEN"));
        // config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
