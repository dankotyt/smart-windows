package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.*;

@Service
@RequiredArgsConstructor
public class WindowLocationConsumer {
    private final WeatherLocationProducer locationProducer;

    @KafkaListener(topics = "window-location-responses", groupId = "weather-group")
    public void consumeLocationResponse(
            @Payload WindowLocationResponseEvent event,
            @Header("X-Request-ID") String requestId) {

        locationProducer.completeRequest(requestId, event.getLocation());
    }
}
