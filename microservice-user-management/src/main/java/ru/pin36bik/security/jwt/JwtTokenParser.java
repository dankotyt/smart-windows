package ru.pin36bik.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import ru.pin36bik.exceptions.InvalidTokenException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class JwtTokenParser {
    private final SecretKey secretKey;
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                return false;
            }
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }

    }

    private boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        if (!blacklistedTokens.contains(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        return blacklistedTokens.contains(token);
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }
}
