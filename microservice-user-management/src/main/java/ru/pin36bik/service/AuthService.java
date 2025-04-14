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
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenParser;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtTokenParser jwtTokenParser;

    public LoginResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .name(registerRequest.getName())
                .surname(registerRequest.getSurname())
                .birthday(registerRequest.getBirthday())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ADMIN) //prod -> только .role(Role.USER)
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
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidTokenException("Refresh токен пустой!");
        }
        String email = jwtTokenParser.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        user.setRefreshToken(null);
        userRepository.save(user);

        return jwtService.generateTokenPair(user);
    }

    public void logout(String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    user.setRefreshToken(null);
                    user.setRefreshTokenExpiry(null);
                    userRepository.save(user);
                });
    }
}
