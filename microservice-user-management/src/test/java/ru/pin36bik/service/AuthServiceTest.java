package ru.pin36bik.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.pin36bik.dto.LoginRequest;
import ru.pin36bik.dto.LoginResponse;
import ru.pin36bik.dto.RegisterRequest;
import ru.pin36bik.entity.User;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.exceptions.UserAlreadyExistsException;
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenParser;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtTokenParser jwtTokenParser;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_WithNewUser_ShouldReturnTokens() {
        // Arrange
        var request = new RegisterRequest();
        request.setName("John");
        request.setSurname("Doe");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setBirthday(null);

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");

        // Используем Answer для эмуляции генерации ID при сохранении
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            return User.builder()
                    .id(1L) // Генерируем ID
                    .name(userToSave.getName())
                    .surname(userToSave.getSurname())
                    .email(userToSave.getEmail())
                    .password(userToSave.getPassword())
                    .role(userToSave.getRole())
                    .build();
        });

        var expectedResponse = new LoginResponse();
        expectedResponse.setAccessToken("access_token");
        expectedResponse.setRefreshToken("refresh_token");

        // Используем any() для User, так как ID генерируется автоматически
        when(jwtService.generateTokenPair(any(User.class))).thenReturn(expectedResponse);

        // Act
        LoginResponse result = authService.register(request);

        // Assert
        assertEquals(expectedResponse, result);
        verify(userRepository).save(any());

        // Дополнительная проверка, что пользователь сохраняется с правильными данными
        verify(userRepository).save(argThat(user ->
                user.getName().equals("John") &&
                        user.getSurname().equals("Doe") &&
                        user.getEmail().equals("test@example.com") &&
                        user.getPassword().equals("encoded_password")
        ));
    }

    @Test
    void register_WithExistingUser_ShouldThrow() {
        // Arrange
        var request = new RegisterRequest();
        request.setName("John");
        request.setSurname("Doe");
        request.setEmail("existing@example.com");
        request.setPassword("password");
        request.setBirthday(null);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.register(request);
        });
    }

    @Test
    void login_WithValidCredentials_ShouldReturnTokens() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = User.builder()
                .email("test@example.com")
                .password("encoded_password")
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded_password")).thenReturn(true);

        var expectedResponse = new LoginResponse();
        expectedResponse.setAccessToken("access_token");
        expectedResponse.setRefreshToken("refresh_token");

        when(jwtService.generateTokenPair(user)).thenReturn(expectedResponse);

        // Act
        LoginResponse result = authService.login(request);

        // Assert
        assertEquals(expectedResponse, result);
    }

    @Test
    void login_WithInvalidUser_ShouldThrow() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("nonexistent@example.com"); // Исправлено на nonexistent
        request.setPassword("password");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    void login_WithInvalidPassword_ShouldThrow() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong_password");

        User user = User.builder()
                .email("test@example.com")
                .password("encoded_password")
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    void refreshToken_WithValidToken_ShouldReturnNewTokens() {
        // Arrange
        String refreshToken = "valid_refresh_token";
        User user = User.builder()
                .email("test@example.com")
                .refreshToken("valid_refresh_token")
                .refreshTokenExpiry(LocalDateTime.now().plusDays(1))
                .build();

        when(jwtTokenParser.isTokenRevoked(refreshToken)).thenReturn(false);
        when(jwtTokenParser.extractUsername(refreshToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtTokenParser.isTokenValid(refreshToken, user)).thenReturn(true);

        var expectedResponse = new LoginResponse();
        expectedResponse.setAccessToken("access_token");
        expectedResponse.setRefreshToken("refresh_token");

        when(jwtService.generateTokenPair(user)).thenReturn(expectedResponse);

        // Act
        LoginResponse result = authService.refreshToken(refreshToken);

        // Assert
        assertEquals(expectedResponse, result);
        verify(jwtTokenParser).revokeToken(refreshToken);
    }

    @Test
    void refreshToken_WithRevokedToken_ShouldThrow() {
        // Arrange
        String refreshToken = "revoked_token";
        when(jwtTokenParser.isTokenRevoked(refreshToken)).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> {
            authService.refreshToken(refreshToken);
        });
    }

    @Test
    void refreshToken_WithExpiredToken_ShouldThrow() {
        // Arrange
        String refreshToken = "expired_token";
        User user = User.builder()
                .email("test@example.com")
                .refreshToken("expired_token")
                .refreshTokenExpiry(LocalDateTime.now().minusDays(1))
                .build();

        when(jwtTokenParser.isTokenRevoked(refreshToken)).thenReturn(false);
        when(jwtTokenParser.extractUsername(refreshToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtTokenParser.isTokenValid(refreshToken, user)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> {
            authService.refreshToken(refreshToken);
        });
    }

    @Test
    void logout_ShouldClearUserTokens() {
        // Arrange
        String refreshToken = "refresh_token";
        User user = User.builder().email("test@example.com").build();
        when(userRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(user));
        when(jwtTokenParser.extractAccessTokenFromRequest()).thenReturn("access_token");

        // Act
        authService.logout(refreshToken);

        // Assert
        verify(jwtTokenParser).revokeToken("access_token");
        verify(userRepository).save(user);
        assertNull(user.getRefreshToken());
    }

    @Test
    void validate_WithValidToken_ShouldNotThrow() {
        // Arrange
        String token = "valid_token";
        User user = User.builder().email("test@example.com").build();
        when(jwtTokenParser.extractUsername(token)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtTokenParser.isTokenValid(token, user)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            authService.validate(token);
        });
    }

    @Test
    void validate_WithInvalidToken_ShouldThrow() {
        // Arrange
        String token = "invalid_token";
        when(jwtTokenParser.extractUsername(token)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> {
            authService.validate(token);
        });
    }
}