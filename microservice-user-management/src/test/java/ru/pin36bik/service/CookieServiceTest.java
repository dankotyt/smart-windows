package ru.pin36bik.service;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import ru.pin36bik.config.JwtConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieServiceTest {

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private CookieService cookieService;

    @Test
    void setAccessTokenCookie_ShouldSetCorrectCookie() {
        // Arrange
        String accessToken = "test-access-token";
        when(jwtConfig.getAccessTtl()).thenReturn(3600000L); // 1 hour in milliseconds

        // Act
        cookieService.setAccessTokenCookie(response, accessToken);

        // Assert
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), headerCaptor.capture());

        String cookieHeader = headerCaptor.getValue();
        assertThat(cookieHeader)
                .contains("__Host-auth-token=test-access-token")
                .contains("HttpOnly")
                .contains("Secure")
                .contains("Path=/")
                .contains("Max-Age=3600")
                .contains("SameSite=Strict");
    }

    @Test
    void setRefreshTokenCookie_ShouldSetCorrectCookie() {
        // Arrange
        String refreshToken = "test-refresh-token";
        when(jwtConfig.getRefreshTtl()).thenReturn(2592000000L); // 30 days in milliseconds

        // Act
        cookieService.setRefreshTokenCookie(response, refreshToken);

        // Assert
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), headerCaptor.capture());

        String cookieHeader = headerCaptor.getValue();
        assertThat(cookieHeader)
                .startsWith("__Host-refresh=test-refresh-token;")
                .contains("Path=/;")
                .contains("Secure;")
                .contains("HttpOnly;")
                .contains("SameSite=Strict");
    }

    @Test
    void expireAllCookies_ShouldExpireBothCookies() {
        // Act
        cookieService.expireAllCookies(response);

        // Assert
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response, times(2)).addHeader(eq(HttpHeaders.SET_COOKIE), headerCaptor.capture());

        List<String> cookieHeaders = headerCaptor.getAllValues();
        assertThat(cookieHeaders)
                .anyMatch(header -> header.startsWith("__Host-auth-token=;") &&
                        header.contains("Path=/;") &&
                        header.contains("Max-Age=0;"))
                .anyMatch(header -> header.startsWith("__Host-refresh=;") &&
                        header.contains("Path=/;") &&
                        header.contains("Max-Age=0;"));
    }

    @Test
    void expireCookie_ShouldSetExpiredCookie() {
        // Act
        cookieService.expireCookie(response, "test-cookie", "/test-path");

        // Assert
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), headerCaptor.capture());

        String cookieHeader = headerCaptor.getValue();
        assertThat(cookieHeader)
                .startsWith("test-cookie=;")
                .contains("Path=/test-path;")
                .contains("Max-Age=0;")
                .containsPattern("Expires=.+"); // Проверяем наличие даты экспирации
    }
}