package ru.pin36bik.config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pin36bik.security.jwt.JwtTokenFactory;
import ru.pin36bik.security.jwt.JwtTokenParser;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private long accessTtl;
    private long refreshTtl;

    @Bean
    public JwtTokenFactory jwtTokenFactory() {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return new JwtTokenFactory(key, accessTtl, refreshTtl);
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public JwtTokenParser jwtTokenParser(SecretKey secretKey) {
        return new JwtTokenParser(secretKey);
    }
}
