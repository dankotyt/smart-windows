package ru.pin36bik.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.pin36bik.entity.User;
import ru.pin36bik.exceptions.InvalidTokenException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenParser {
    private final SecretKey secretKey;
    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new InvalidTokenException("Неверный JWT токен: " + e.getMessage());
        }
    }

    public boolean isTokenValid(String token, User user) {
        try {
            final String username = extractUsername(token);
            boolean isBasicValid = username.equals(user.getEmail())
                    && !isTokenExpired(token)
                    && !isTokenRevoked(token);


            return isBasicValid;
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public void revokeToken(String token) {
        if (!isTokenExpired(token)) {
            revokedTokens.add(token);
        }
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }

    public String extractAccessTokenFromRequest() {
        try {
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();

            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }

            return null;
        } catch (IllegalStateException e) {
            log.warn("Request context not available");
            return null;
        }
    }
}
