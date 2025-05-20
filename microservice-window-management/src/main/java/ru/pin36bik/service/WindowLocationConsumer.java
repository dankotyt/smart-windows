package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.WindowLocationDTO;
import ru.pin36bik.dto.WindowLocationRequestEvent;
import ru.pin36bik.dto.WindowLocationResponseEvent;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class WindowLocationConsumer {
    private final WindowService windowService;
    private final KafkaTemplate<String, WindowLocationResponseEvent> kafkaTemplate;

    @KafkaListener(topics = "window-location-requests", groupId = "window-group")
    public void consumeLocationRequest(
            @Payload WindowLocationRequestEvent event,
            @Header("X-Request-ID") String requestId) {
        try {
            WindowLocationDTO location = windowService.getLocation(event.getWindowId(), event.getUserEmail());
            var response = WindowLocationResponseEvent.builder()
                    .requestId(requestId)
                    .windowId(event.getWindowId())
                    .location(location)
                    .timestamp(Instant.now())
                    .build();

            ProducerRecord<String, WindowLocationResponseEvent> record =
                    new ProducerRecord<>("window-location-responses",
                            event.getWindowId().toString(), response);
            record.headers().add("X-Request-ID", requestId.getBytes());
            kafkaTemplate.send(record);
        } catch (IllegalStateException e) {
            log.error("Failed to process location request", e);
        }
    }
}
