package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.PresetDTO;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, PresetDTO> kafkaTemplate;

    public void sendPresetResponse(final PresetDTO presetDTO) {
        try {
            kafkaTemplate.send("preset-response-topic", presetDTO);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send preset to Kafka", e);
        }
    }
}
