package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.LoginResponse;
import ru.pin36bik.dto.LoginRequest;
import ru.pin36bik.dto.RegisterRequest;
import ru.pin36bik.entity.User;
import ru.pin36bik.entity.role.Role;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.exceptions.UserAlreadyExistsException;
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenParser;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtTokenParser jwtTokenParser;

    public LoginResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с email " + registerRequest.getEmail() + " уже существует");
        }
        var user = User.builder()
                .name(registerRequest.getName())
                .surname(registerRequest.getSurname())
                .birthday(registerRequest.getBirthday())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER) //prod -> только .role(Role.USER)
                .build();
        userRepository.save(user);
        return jwtService.generateTokenPair(user);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Неверный пароль");
        }

        return jwtService.generateTokenPair(user);
    }

    public LoginResponse refreshToken(String refreshToken) {

        if (jwtTokenParser.isTokenRevoked(refreshToken)) {
            throw new InvalidTokenException("Token revoked");
        }

        String email = jwtTokenParser.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidTokenException("Refresh токен пустой!");
        }

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new InvalidTokenException("Несоответствие refresh токена");
        }

        if (!jwtTokenParser.isTokenValid(refreshToken, user)) {
            if (user.getRefreshTokenExpiry() != null &&
                    user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new InvalidTokenException("Срок действия refresh токена истек");
            }
            throw new InvalidTokenException("Refresh токен недействителен или истек");
        }
        LoginResponse tokens = jwtService.generateTokenPair(user);
        jwtTokenParser.revokeToken(refreshToken);
        return tokens;
    }

    public void logout(String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String accessToken = jwtTokenParser.extractAccessTokenFromRequest();
                    if (accessToken != null) {
                        jwtTokenParser.revokeToken(accessToken);
                    };
                    user.setRefreshToken(null);
                    userRepository.save(user);
                });
    }

    public void validate(String token) {
        String email = jwtTokenParser.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException("User not found"));

        if (!jwtTokenParser.isTokenValid(token, user)) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}
