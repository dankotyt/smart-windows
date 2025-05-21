package ru.pin36bik.filter;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.crypto.SecretKey;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GatewayJwtTokenParserTest {

    private GatewayJwtTokenParser jwtTokenParser;
    private SecretKey secretKey;
    private String validToken;
    private String invalidToken = "invalid.token.here";

    @BeforeEach
    public void setUp() {
        secretKey = Keys.hmacShaKeyFor("testSecretKeyWhichIsLongEnoughForHS512Algorithm".getBytes());
        jwtTokenParser = new GatewayJwtTokenParser(secretKey);

        validToken = Jwts.builder()
                .subject("test@example.com")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(secretKey)
                .compact();
    }

    @Test
    public void testIsTokenValid_WithValidToken_ReturnsTrue() {
        // Act & Assert
        assertTrue(jwtTokenParser.isTokenValid(validToken));
    }

    @Test
    public void testIsTokenValid_WithInvalidToken_ReturnsFalse() {
        // Act & Assert
        assertFalse(jwtTokenParser.isTokenValid(invalidToken));
    }

    @Test
    public void testExtractEmail_WithValidToken_ReturnsEmail() {
        // Act
        String email = jwtTokenParser.extractEmail(validToken);

        // Assert
        assertEquals("test@example.com", email);
    }

    @Test
    public void testExtractEmail_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtTokenParser.extractEmail(invalidToken));
    }
}