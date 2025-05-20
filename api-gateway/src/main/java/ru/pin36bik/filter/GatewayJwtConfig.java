package ru.pin36bik.filter;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
@Getter
@Setter
public class GatewayJwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Bean("gatewaySecretKey")
    public SecretKey secretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secret.trim());
        return Keys.hmacShaKeyFor(decodedKey);
    }

    @Bean("gatewayJwtTokenParser")
    public GatewayJwtTokenParser jwtTokenParser(SecretKey secretKey) {
        return new GatewayJwtTokenParser(secretKey);
    }
}
