package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pin36bik.config.JwtConfig;
import ru.pin36bik.dto.AuthResponseDTO;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserLoginDTO;
import ru.pin36bik.entity.User;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenFactory;
import ru.pin36bik.security.jwt.JwtTokenParser;
import ru.pin36bik.utils.UserMapper;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;
    private final JwtTokenFactory jwtTokenFactory;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtTokenParser jwtTokenParser;
    private final UserDTO userDTO;
    private final UserMapper userMapper;






    public boolean isRefreshTokenValid(String email, String refreshToken) {
        User user = getUserByEmail(email);
        return refreshToken.equals(user.getRefreshToken()) &&
                LocalDateTime.now().isBefore(user.getRefreshTokenExpiry());
    }

    public void updateRefreshToken(String email, String refreshToken, LocalDateTime expiry) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(expiry);
        userRepository.save(user);
    }

    public AuthResponseDTO authenticate(UserLoginDTO dto) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        return createNewTokenPair(user);
    }

    public AuthResponseDTO refreshToken(String refreshToken) {
        if (!jwtTokenParser.isTokenValid(refreshToken)) {
            throw new InvalidTokenException("Некорректный refresh token");
        }

        if (jwtTokenParser.isTokenBlacklisted(refreshToken)) {
            throw new InvalidTokenException("Токен отозван");
        }

        String email = jwtTokenParser.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        if (!refreshToken.equals(user.getRefreshToken()) ||
                LocalDateTime.now().isAfter(user.getRefreshTokenExpiry())) {
            throw new InvalidTokenException("Срок действия токена истек");
        }

        return createNewTokenPair(user);
    }

    private AuthResponseDTO createNewTokenPair(User user) {
        String newAccessToken = jwtTokenFactory.createAccessToken(user);
        String newRefreshToken = jwtTokenFactory.createRefreshToken(user);
        LocalDateTime refreshExpiry = LocalDateTime.now().plusSeconds(jwtConfig.getRefreshTtl() / 100);
        updateRefreshToken(user.getEmail(), newRefreshToken, refreshExpiry);
        userRepository.save(user);
        return new AuthResponseDTO(
                newAccessToken, // Access token тоже нужен!
                userMapper.toDTO(user)
        );
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
