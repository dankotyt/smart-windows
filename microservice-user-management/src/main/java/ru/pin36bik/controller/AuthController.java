package ru.pin36bik.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.dto.RegisterRequest;
import ru.pin36bik.dto.LoginRequest;
import ru.pin36bik.dto.LoginResponse;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.service.AuthService;
import ru.pin36bik.service.CookieService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = authService.register(request);
        cookieService.setRefreshTokenCookie(response, loginResponse.getRefreshToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = authService.login(request);
        cookieService.setRefreshTokenCookie(response, loginResponse.getRefreshToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(value = "__Host-refresh", required = false) String refreshToken,
            HttpServletResponse response) {
        if (refreshToken == null) {
            throw new InvalidTokenException("Refresh token required");
        }
        try {
            LoginResponse tokens = authService.refreshToken(refreshToken);

            cookieService.setAccessTokenCookie(response, tokens.getAccessToken());
            cookieService.setRefreshTokenCookie(response, tokens.getRefreshToken());

            return ResponseEntity.ok(tokens);
        } catch (InvalidTokenException e) {
            cookieService.expireAllCookies(response);
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("__Host-refresh") String refreshToken,
            HttpServletResponse response) {

        authService.logout(refreshToken);
        cookieService.expireAllCookies(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            String token = authHeader.substring(7);
            log.debug("Validating token: {}", token);
            authService.validate(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Token validation failed", e);
            throw e;
        }
    }
}
