package ru.pin36bik.filter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.client.reactive.MockClientHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GatewayJwtFilterTest {

    @Mock
    private GatewayJwtTokenParser gatewayJwtTokenParser;

    @Mock
    private WebClient webClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @Mock
    private GatewayFilterChain filterChain;

    @InjectMocks
    private GatewayJwtFilter gatewayJwtFilter;

    private void setupWebClientMock() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.exchangeToMono(any())).thenReturn(Mono.empty());
    }

    @Test
    public void testApply_AuthEndpoint_ShouldPassThrough() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/auth/login").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(filterChain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = gatewayJwtFilter.apply(new GatewayJwtFilter.Config())
                .filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyComplete();

        verify(filterChain).filter(exchange);
        verifyNoInteractions(gatewayJwtTokenParser);
    }

    @Test
    public void testApply_NoToken_ShouldReturnUnauthorized() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/protected").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // Act
        Mono<Void> result = gatewayJwtFilter.apply(new GatewayJwtFilter.Config())
                .filter(exchange, filterChain);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        assertNotNull(exchange.getResponse().getHeaders().getFirst("X-Auth-Error"));
    }

    @Test
    public void testApply_InvalidToken_ShouldReturnUnauthorized() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(gatewayJwtTokenParser.isTokenValid("invalid.token")).thenReturn(false);

        // Act
        Mono<Void> result = gatewayJwtFilter.apply(new GatewayJwtFilter.Config())
                .filter(exchange, filterChain);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        assertNotNull(exchange.getResponse().getHeaders().getFirst("X-Auth-Error"));
    }

    @Test
    public void testApply_ValidToken_ShouldAddHeadersAndContinue() {
        // Arrange
        String validToken = "valid.token";
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/protected")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                        .build()
        );

        // Настройка WebClient mock
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8082/api/v1/auth/validate")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)).thenReturn(requestHeadersSpec);

        // Исправленная часть - правильное мокирование ответа
        when(requestHeadersSpec.exchangeToMono(any())).thenAnswer(invocation -> {
            Function<ClientResponse, Mono<Void>> handler = invocation.getArgument(0);
            ClientResponse clientResponse = mock(ClientResponse.class);
            when(clientResponse.statusCode()).thenReturn(HttpStatus.OK);
            return handler.apply(clientResponse);
        });

        // Настройка парсера JWT
        when(gatewayJwtTokenParser.isTokenValid(validToken)).thenReturn(true);
        when(gatewayJwtTokenParser.extractEmail(validToken)).thenReturn("user@example.com");

        // Настройка цепочки фильтров
        when(filterChain.filter(any())).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = gatewayJwtFilter.apply(new GatewayJwtFilter.Config())
                .filter(exchange, filterChain);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        // Проверяем вызовы
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8082/api/v1/auth/validate");
        verify(requestHeadersSpec).header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken);
        verify(filterChain).filter(any());

        // Проверяем добавленные заголовки
        ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(ServerWebExchange.class);
        verify(filterChain).filter(exchangeCaptor.capture());

        ServerHttpRequest modifiedRequest = exchangeCaptor.getValue().getRequest();
        assertEquals("true", modifiedRequest.getHeaders().getFirst("X-Valid-Token"));
        assertEquals("user@example.com", modifiedRequest.getHeaders().getFirst("X-User-Email"));
    }
}