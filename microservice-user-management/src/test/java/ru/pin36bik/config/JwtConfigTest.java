package ru.pin36bik.config;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenFactory;
import ru.pin36bik.security.jwt.JwtTokenParser;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
class JwtConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Mock
    private AuthenticationConfiguration authConfig;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    private final String testSecret = Base64.getEncoder().encodeToString("test-secret-key-1234567890".getBytes());

//    @Test
//    void shouldCreateAllBeansWithValidConfig() {
//        contextRunner
//                .withPropertyValues(
//                        "jwt.secret=" + testSecret,
//                        "jwt.access-ttl=3600",
//                        "jwt.refresh-ttl=86400"
//                )
//                .withBean(UserRepository.class, () -> userRepository)
//                .withBean(AuthenticationConfiguration.class, () -> authConfig)
//                .withUserConfiguration(JwtConfig.class)
//                .run(context -> {
//                    assertThat(context).hasNotFailed();
//                    // Проверяем создание бина SecretKey
//                    SecretKey secretKey = context.getBean("userManagementSecretKey", SecretKey.class);
//                    assertThat(secretKey).isNotNull();
//                    assertThat(secretKey.getAlgorithm()).isEqualTo("HmacSHA256");
//
//                    // Проверяем создание бина JwtTokenParser
//                    JwtTokenParser jwtTokenParser = context.getBean("userManagementJwtTokenParser", JwtTokenParser.class);
//                    assertThat(jwtTokenParser).isNotNull();
//
//                    // Проверяем создание бина JwtTokenFactory
//                    JwtTokenFactory jwtTokenFactory = context.getBean(JwtTokenFactory.class);
//                    assertThat(jwtTokenFactory).isNotNull();
//
//                    // Проверяем создание бина AuthenticationManager
//                    AuthenticationManager authManager = context.getBean(AuthenticationManager.class);
//                    assertThat(authManager).isEqualTo(authenticationManager);
//                });
//    }

//    @Test
//    void secretKeyBeanShouldUseConfiguredSecret() {
//        contextRunner
//                .withPropertyValues("jwt.secret=" + testSecret)
//                .withUserConfiguration(JwtConfig.class)
//                .run(context -> {
//                    SecretKey secretKey = context.getBean("userManagementSecretKey", SecretKey.class);
//                    byte[] decodedKey = Base64.getDecoder().decode(testSecret.trim());
//                    SecretKey expectedKey = Keys.hmacShaKeyFor(decodedKey);
//                    assertThat(secretKey).isEqualTo(expectedKey);
//                });
//    }
//
//    @Test
//    void jwtTokenFactoryShouldBeConfiguredWithCorrectTtlValues() {
//        contextRunner
//                .withPropertyValues(
//                        "jwt.secret=" + testSecret,
//                        "jwt.access-ttl=1800",
//                        "jwt.refresh-ttl=7200"
//                )
//                .withUserConfiguration(JwtConfig.class)
//                .run(context -> {
//                    JwtTokenFactory jwtTokenFactory = context.getBean(JwtTokenFactory.class);
//                    assertThat(jwtTokenFactory).isNotNull();
//
//                    // Проверяем, что значения TTL установлены правильно
//                    // Предполагая, что в JwtTokenFactory есть соответствующие геттеры
//                    assertThat(jwtTokenFactory.getAccessTtl()).isEqualTo(1800);
//                    assertThat(jwtTokenFactory.getRefreshTtl()).isEqualTo(7200);
//                });
//    }

    @Test
    void shouldFailWhenSecretIsNotConfigured() {
        contextRunner
                .withUserConfiguration(JwtConfig.class)
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context.getStartupFailure().getMessage())
                            .contains("Cannot invoke \"String.trim()\" because \"this.secret\" is null");
                });
    }

    @Test
    void shouldFailWhenSecretIsInvalid() {
        contextRunner
                .withPropertyValues("jwt.secret=invalid-base64-string")
                .withUserConfiguration(JwtConfig.class)
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context.getStartupFailure().getMessage())
                            .contains("Failed to instantiate [javax.crypto.SecretKey]");
                });
    }
}