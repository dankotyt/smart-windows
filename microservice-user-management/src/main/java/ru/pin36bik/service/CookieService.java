package ru.pin36bik.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import ru.pin36bik.config.JwtConfig;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final JwtConfig jwtConfig;

    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from("__Host-auth-token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge((int) jwtConfig.getAccessTtl() / 1000)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("__Host-refresh", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge((int) jwtConfig.getRefreshTtl() / 1000)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void expireAllCookies(HttpServletResponse response) {
        expireCookie(response, "__Host-auth-token", "/");
        expireCookie(response, "__Host-refresh", "/api/v1/auth/refresh");
    }

    private void expireCookie(HttpServletResponse response, String name, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .maxAge(0)
                .path(path)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
