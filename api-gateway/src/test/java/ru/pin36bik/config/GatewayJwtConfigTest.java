package ru.pin36bik.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.pin36bik.filter.GatewayJwtTokenParser;

import javax.crypto.SecretKey;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=testSecretKeyWhichIsLongEnoughForHS512Algorithm",
        "YWAPI_KEY=mock-api-key"
})
public class GatewayJwtConfigTest {

    @Autowired
    private GatewayJwtConfig gatewayJwtConfig;

    @Test
    public void testSecretKeyBeanCreation() {
        SecretKey secretKey = gatewayJwtConfig.secretKey();
        assertNotNull(secretKey);
    }

    @Test
    public void testJwtTokenParserBeanCreation() {
        GatewayJwtTokenParser parser = gatewayJwtConfig.jwtTokenParser(gatewayJwtConfig.secretKey());
        assertNotNull(parser);
    }
}