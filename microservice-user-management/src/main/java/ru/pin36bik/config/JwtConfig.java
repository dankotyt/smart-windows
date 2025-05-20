package ru.pin36bik.config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenFactory;
import ru.pin36bik.security.jwt.JwtTokenParser;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private long accessTtl;
    private long refreshTtl;
    private UserRepository userRepository;

    @Bean
    public JwtTokenFactory jwtTokenFactory(SecretKey secretKey) {
        return new JwtTokenFactory(secretKey, accessTtl, refreshTtl);
    }

    @Bean("userManagementSecretKey")
    @Primary
    public SecretKey secretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secret.trim());
        return Keys.hmacShaKeyFor(decodedKey);
    }

    @Bean("userManagementJwtTokenParser")
    @Primary
    public JwtTokenParser jwtTokenParser(SecretKey secretKey) {
        return new JwtTokenParser(secretKey);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
