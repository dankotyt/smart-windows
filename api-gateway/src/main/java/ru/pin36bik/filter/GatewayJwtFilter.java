package ru.pin36bik.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class GatewayJwtFilter implements GatewayFilterFactory<GatewayJwtFilter.Config> {

    private final GatewayJwtTokenParser gatewayJwtTokenParser;
    private final WebClient webClient;

    public static class Config {
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Incoming request to: {}", exchange.getRequest().getPath());

            if (isAuthEndpoint(exchange.getRequest())) {
                return chain.filter(exchange);
            }

            String accessToken = extractAccessToken(exchange.getRequest());
            if (accessToken == null) {
                return unauthorized(exchange, "Access token is required");
            }

            if (!gatewayJwtTokenParser.isTokenValid(accessToken)) {
                return unauthorized(exchange, "Invalid token");
            }
            log.info("Validating token with auth service: {}", accessToken);
            return webClient.get()
                    .uri("http://localhost:8082/api/v1/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .exchangeToMono(clientResponse -> {
                        log.info("Validation response status: {}", clientResponse.statusCode());
                        if (!clientResponse.statusCode().is2xxSuccessful()) {
                            return unauthorized(exchange, "Invalid token");
                        }

                        ServerHttpRequest request = exchange.getRequest().mutate()
                                .header("X-Valid-Token", "true")
                                .header("X-User-Email", gatewayJwtTokenParser.extractEmail(accessToken))
                                .build();

                        return chain.filter(exchange.mutate().request(request).build());
                    });
        };
    }

    private boolean isAuthEndpoint(ServerHttpRequest request) {
        return request.getPath().toString().startsWith("/api/v1/auth");
    }

    private String extractAccessToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String errorMessage) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("X-Auth-Error", errorMessage);
        return exchange.getResponse().setComplete();
    }
}

