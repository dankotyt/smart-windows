package ru.pin36bik.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebClientConfigTest {

    @Test
    public void testWebClientBeanCreation() {
        // Arrange
        WebClientConfig config = new WebClientConfig();

        // Act
        WebClient webClient = config.webClient();

        // Assert
        assertNotNull(webClient);
    }
}