package ru.pin36bik.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pin36bik.entity.User;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenParser;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String REFRESH_TOKEN_COOKIE = "__Host-refresh";

    private final JwtTokenParser jwtTokenParser;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().startsWith("/api/v1/auth/register") ||
                request.getServletPath().startsWith("/api/v1/auth/login") ||
                request.getServletPath().startsWith("/api/v1/auth/validate")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = extractAccessToken(request);
            if (accessToken == null) {
                throw new InvalidTokenException("Access token required");
            }
            String username = jwtTokenParser.extractUsername(accessToken);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new InvalidTokenException("User not found"));

            // Включить обратно проверку валидности токена
            if (!jwtTokenParser.isTokenValid(accessToken, user)) {
                throw new InvalidTokenException("Invalid access token");
            }

            // Для всех запросов, кроме refresh, проверяем refresh token
            if (!request.getServletPath().equals("/api/v1/auth/refresh")) {
                String refreshToken = extractRefreshToken(request);

                if (refreshToken == null) {
                    throw new InvalidTokenException("Refresh token required");
                }

                if (!refreshToken.equals(user.getRefreshToken())) {
                    throw new InvalidTokenException("Invalid refresh token");
                }
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (InvalidTokenException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    private String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("_Host-auth-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}