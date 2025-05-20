package ru.pin36bik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.PresetDTO;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @Autowired
    public KafkaProducerService(final KafkaTemplate<String,
            PresetDTO> myKafkaTemplate) {
        this.kafkaTemplate = myKafkaTemplate;
    }

    public void sendPresetResponse(final PresetDTO presetDTO) {
        try {
            kafkaTemplate.send("preset-response-topic", presetDTO);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send preset to Kafka", e);
        }
    }
}
