package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.WindowLocationDTO;
import ru.pin36bik.dto.WindowLocationRequestEvent;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherLocationProducer {
    private final KafkaTemplate<String, WindowLocationRequestEvent> kafkaTemplate;
    private final Map<String, CompletableFuture<WindowLocationDTO>> pendingRequests = new ConcurrentHashMap<>();

    public CompletableFuture<WindowLocationDTO> requestLocation(Long windowId, String userEmail) {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<WindowLocationDTO> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        var event = WindowLocationRequestEvent.builder()
                .requestId(requestId)
                .windowId(windowId)
                .userEmail(userEmail)
                .timestamp(Instant.now())
                .build();

        ProducerRecord<String, WindowLocationRequestEvent> record =
                new ProducerRecord<>("window-location-requests", windowId.toString(), event);
        record.headers().add("X-Request-ID", requestId.getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        pendingRequests.remove(requestId);
                        future.completeExceptionally(ex);
                        log.error("Failed to send location request for window {}", windowId, ex);
                    } else {
                        log.debug("Location request sent for window {}", windowId);
                    }
                });

        return future;
    }

    public void completeRequest(String requestId, WindowLocationDTO location) {
        CompletableFuture<WindowLocationDTO> future = pendingRequests.remove(requestId);
        if (future != null) {
            future.complete(location);
        }
    }
}
