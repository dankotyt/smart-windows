package ru.pin36bik.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import ru.pin36bik.dto.LoginRequest;
import ru.pin36bik.dto.LoginResponse;
import ru.pin36bik.dto.RegisterRequest;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.service.AuthService;
import ru.pin36bik.service.CookieService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private CookieService cookieService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_ShouldReturnTokensAndSetCookie() {
        // Arrange
        var request = new RegisterRequest();
        request.setName("John");
        request.setSurname("Gargield");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setBirthday(null);

        var expectedResponse = new LoginResponse();
        expectedResponse.setRefreshToken("refresh");
        expectedResponse.setAccessToken("access");

        when(authService.register(any())).thenReturn(expectedResponse);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        ResponseEntity<LoginResponse> result = authController.register(request, response);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(cookieService).setRefreshTokenCookie(response, "refresh");
    }

    @Test
    void login_ShouldReturnTokensAndSetCookie() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        var expectedResponse = new LoginResponse();
        expectedResponse.setRefreshToken("refresh");
        expectedResponse.setAccessToken("access");
        when(authService.login(any())).thenReturn(expectedResponse);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        ResponseEntity<LoginResponse> result = authController.login(request, response);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(cookieService).setRefreshTokenCookie(response, "refresh");
    }

    @Test
    void refresh_WithValidToken_ShouldReturnNewTokensAndSetCookies() {
        // Arrange
        String refreshToken = "valid_refresh_token";
        var expectedResponse = new LoginResponse();
        expectedResponse.setRefreshToken("new_refresh");
        expectedResponse.setAccessToken("new_access");
        when(authService.refreshToken(refreshToken)).thenReturn(expectedResponse);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        ResponseEntity<LoginResponse> result = authController.refresh(refreshToken, response);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(cookieService).setAccessTokenCookie(response, "new_access");
        verify(cookieService).setRefreshTokenCookie(response, "new_refresh");
    }

    @Test
    void refresh_WithInvalidToken_ShouldExpireCookiesAndThrow() {
        // Arrange
        String refreshToken = "invalid_token";
        when(authService.refreshToken(refreshToken)).thenThrow(new InvalidTokenException("Invalid token"));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> {
            authController.refresh(refreshToken, response);
        });
        verify(cookieService).expireAllCookies(response);
    }

    @Test
    void logout_ShouldCallServiceAndExpireCookies() {
        // Arrange
        String refreshToken = "refresh_token";
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        ResponseEntity<Void> result = authController.logout(refreshToken, response);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authService).logout(refreshToken);
        verify(cookieService).expireAllCookies(response);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnOk() {
        // Arrange
        String authHeader = "Bearer valid_token";
        doNothing().when(authService).validate("valid_token");

        // Act
        ResponseEntity<Void> result = authController.validateToken(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void validateToken_WithInvalidToken_ShouldThrow() {
        // Arrange
        String authHeader = "Bearer invalid_token";
        doThrow(new InvalidTokenException("Invalid token")).when(authService).validate("invalid_token");

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> {
            authController.validateToken(authHeader);
        });
    }
}