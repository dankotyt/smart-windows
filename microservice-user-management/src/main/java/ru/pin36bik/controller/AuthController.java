package ru.pin36bik.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.config.JwtConfig;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.LoginRequest;
import ru.pin36bik.dto.AuthResponse;
import ru.pin36bik.entity.User;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.security.jwt.JwtTokenFactory;
import ru.pin36bik.service.AuthService;
import ru.pin36bik.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtConfig jwtConfig;
    private final JwtTokenFactory jwtTokenFactory;
    private final AuthService authService;

//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> register(
//            @RequestBody RegisterRequest request
//    ) {
//        AuthResponseDTO authUserDTO = authService.authenticate(loginDTO);
//        User user = userService.getUserByEmail(loginDTO.getEmail());
//
//        String accessToken = jwtTokenFactory.createAccessToken(user);
//        String refreshToken = jwtTokenFactory.createRefreshToken(user);
//
//        LocalDateTime refreshExpiry = LocalDateTime.now().plusSeconds(jwtConfig.getRefreshTtl() / 100);
//        authService.updateRefreshToken(user.getEmail(), refreshToken, refreshExpiry);
//
//        response.addCookie(createRefreshTokenCookie(refreshToken));
//
//        UserDTO userDTO = authUserDTO.getUserDTO();
//
//        return ResponseEntity.ok(new AuthResponse(accessToken, userDTO));
//    }
//
//    @PostMapping("/authenticate")
//    public ResponseEntity<AuthResponse> authenticate(
//            @RequestBody AuthenticationRequest request
//    ) {
//
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest loginDTO,
            HttpServletResponse response
    ) {
        AuthResponse authUserDTO = authService.authenticate(loginDTO);
        User user = userService.getUserByEmail(loginDTO.getEmail());

        String accessToken = jwtTokenFactory.createAccessToken(user);
        String refreshToken = jwtTokenFactory.createRefreshToken(user);

        LocalDateTime refreshExpiry = LocalDateTime.now().plusSeconds(jwtConfig.getRefreshTtl() / 100);
        authService.updateRefreshToken(user.getEmail(), refreshToken, refreshExpiry);

        response.addCookie(createRefreshTokenCookie(refreshToken));

        UserDTO userDTO = authUserDTO.getUserDTO();

        return ResponseEntity.ok(new AuthResponse(accessToken, userDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue("__Host-refresh") String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            response.addCookie(createExpiredCookie());
            throw new InvalidTokenException("Refresh token отсутствует!");
        }
        try {
            AuthResponse authResponse = authService.refreshToken(refreshToken);

            response.addCookie(createSecureCookie(
                    "__Host-refresh",
                    refreshToken,
                    Duration.ofMillis(jwtConfig.getRefreshTtl())
            ));

            return ResponseEntity.ok(authResponse);
        } catch (InvalidTokenException e) {
            response.addCookie(createExpiredCookie());
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("__Host-refresh") String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken);
        response.addCookie(createExpiredCookie());
        return ResponseEntity.ok().build();
    }

    private Cookie createSecureCookie(String name, String value, Duration maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAge.toSeconds());
        return cookie;
    }

    private Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie("__Host-refresh", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) jwtConfig.getRefreshTtl() / 100); // 30 дней
        return cookie;
    }

    protected Cookie createExpiredCookie() {
        Cookie cookie = new Cookie("__Host-jwt", null);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
