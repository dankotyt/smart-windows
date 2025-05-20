package ru.pin36bik.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import ru.pin36bik.entity.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFactory {

    private final SecretKey secretKey;
    private final long accessTtl;
    private final long refreshTtl;

    public String createAccessToken(User user, List<String> authorities) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusMillis(accessTtl)))
                .claim("authorities", authorities)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(refreshTtl)))
                .signWith(secretKey)
                .compact();
    }
}