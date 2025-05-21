package ru.pin36bik.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.pin36bik.entity.User;
import ru.pin36bik.entity.role.Role;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenParser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.pin36bik.entity.role.Role.USER;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtTokenParser jwtTokenParser;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JwtAuthFilter(jwtTokenParser, userRepository);
    }

    @Test
    void doFilterInternal_ShouldSkipAuthEndpoints() throws Exception {
        when(request.getServletPath()).thenReturn("/api/v1/auth/login");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtTokenParser, userRepository);
    }

    @Test
    void doFilterInternal_ShouldRejectRequestWithoutAccessToken() throws Exception {
        when(request.getServletPath()).thenReturn("/api/v1/users/me");
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpStatus.UNAUTHORIZED.value(), "Access token required");
        verifyNoInteractions(filterChain);
    }

    @Test
    void doFilterInternal_ShouldAuthenticateWithValidTokens() throws Exception {
        // Arrange
        User user = new User();
        user.setEmail("user@test.com");
        user.setRefreshToken("valid-refresh-token");
        user.setRole(USER);

        when(request.getServletPath()).thenReturn("/api/v1/users/me");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-access-token");
        when(request.getCookies()).thenReturn(new Cookie[] {
                new Cookie("__Host-refresh", "valid-refresh-token")
        });

        when(jwtTokenParser.extractUsername("valid-access-token")).thenReturn("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtTokenParser.isTokenValid("valid-access-token", user)).thenReturn(true);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user@test.com",
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void doFilterInternal_ShouldRejectInvalidAccessToken() throws Exception {
        User user = new User();
        user.setEmail("user@test.com");

        when(request.getServletPath()).thenReturn("/api/v1/users/me");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtTokenParser.extractUsername("invalid-token")).thenReturn("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtTokenParser.isTokenValid("invalid-token", user)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid access token");
        verifyNoInteractions(filterChain);
    }

    @Test
    void doFilterInternal_ShouldRejectInvalidRefreshToken() throws Exception {
        User user = new User();
        user.setEmail("user@test.com");
        user.setRefreshToken("valid-refresh-token");

        when(request.getServletPath()).thenReturn("/api/v1/users/me");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(request.getCookies()).thenReturn(new Cookie[] {
                new Cookie("__Host-refresh", "invalid-refresh-token")
        });

        when(jwtTokenParser.extractUsername("valid-token")).thenReturn("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtTokenParser.isTokenValid("valid-token", user)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token");
    }

    @Test
    void doFilterInternal_ShouldSkipRefreshTokenCheckForRefreshEndpoint() throws Exception {
        User user = new User();
        user.setEmail("user@test.com");
        user.setRole(USER);

        when(request.getServletPath()).thenReturn("/api/v1/auth/refresh");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");

        when(jwtTokenParser.extractUsername("valid-token")).thenReturn("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtTokenParser.isTokenValid("valid-token", user)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(request, never()).getCookies(); // Не должен запрашивать куки для refresh endpoint
    }

    @Test
    void extractAccessToken_ShouldExtractFromHeader() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token-from-header");

        String token = jwtAuthFilter.extractAccessToken(request);

        assertEquals("token-from-header", token);
    }

    @Test
    void extractAccessToken_ShouldExtractFromCookie() {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getCookies()).thenReturn(new Cookie[] {
                new Cookie("_Host-auth-token", "token-from-cookie")
        });

        String token = jwtAuthFilter.extractAccessToken(request);

        assertEquals("token-from-cookie", token);
    }

    @Test
    void extractRefreshToken_ShouldReturnNullWhenNoCookies() {
        when(request.getCookies()).thenReturn(null);

        assertNull(jwtAuthFilter.extractRefreshToken(request));
    }

    @Test
    void extractRefreshToken_ShouldExtractCorrectCookie() {
        when(request.getCookies()).thenReturn(new Cookie[] {
                new Cookie("wrong-cookie", "value"),
                new Cookie("__Host-refresh", "refresh-token-value")
        });

        assertEquals("refresh-token-value", jwtAuthFilter.extractRefreshToken(request));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}