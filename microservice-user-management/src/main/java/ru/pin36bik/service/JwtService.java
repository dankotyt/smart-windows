package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pin36bik.config.JwtConfig;
import ru.pin36bik.dto.LoginResponse;
import ru.pin36bik.entity.User;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenFactory;
import ru.pin36bik.utils.UserMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;
    private final JwtTokenFactory jwtTokenFactory;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;

    protected LoginResponse generateTokenPair(User user) {
        String accessToken = jwtTokenFactory.createAccessToken(user);
        String refreshToken = jwtTokenFactory.createRefreshToken(user);

        LocalDateTime expiry = LocalDateTime.now().plusSeconds(jwtConfig.getRefreshTtl() / 1000);
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(expiry);
        userRepository.save(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                userMapper.toDTO(user)
        );
    }
}
