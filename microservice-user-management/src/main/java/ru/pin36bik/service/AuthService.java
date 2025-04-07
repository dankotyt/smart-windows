package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.pin36bik.config.JwtConfig;
import ru.pin36bik.dto.AuthResponse;
import ru.pin36bik.dto.LoginRequest;
import ru.pin36bik.entity.User;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenFactory;
import ru.pin36bik.security.jwt.JwtTokenParser;
import ru.pin36bik.utils.UserMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;
    private final JwtTokenFactory jwtTokenFactory;
    private final AuthenticationManager authManager;
    private final JwtTokenParser jwtTokenParser;
    private final UserMapper userMapper;

//    public boolean isRefreshTokenValid(String email, String refreshToken) {
//        User user = getUserByEmail(email);
//        return refreshToken.equals(user.getRefreshToken()) &&
//                LocalDateTime.now().isBefore(user.getRefreshTokenExpiry());
//    }

    public AuthResponse authenticate(LoginRequest loginDTO) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        return createNewTokenPair(user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtTokenParser.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        if (!jwtTokenParser.isTokenValid(refreshToken, user)) {
            throw new InvalidTokenException("Некорректный refresh token");
        }

        if (!refreshToken.equals(user.getRefreshToken()) ||
                LocalDateTime.now().isAfter(user.getRefreshTokenExpiry())) {
            throw new InvalidTokenException("Срок действия токена истек");
        }

        return createNewTokenPair(user);
    }

    public void updateRefreshToken(String email, String refreshToken, LocalDateTime expiry) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(expiry);
        userRepository.save(user);
    }

    public void logout(String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    user.setRefreshToken(null);
                    userRepository.save(user);
                });
    }

    private AuthResponse createNewTokenPair(User user) {
        String newAccessToken = jwtTokenFactory.createAccessToken(user);
        String newRefreshToken = jwtTokenFactory.createRefreshToken(user);

        LocalDateTime refreshExpiry = LocalDateTime.now().plusSeconds(jwtConfig.getRefreshTtl() / 100);
        updateRefreshToken(user.getEmail(), newRefreshToken, refreshExpiry);

        return new AuthResponse(
                newAccessToken, // Access token тоже нужен!
                userMapper.toDTO(user)
        );
    }
}
