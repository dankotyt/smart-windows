package ru.pin36bik.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pin36bik.dto.PresetDTO;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, PresetDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPresetResponse(PresetDTO presetDTO) {
        kafkaTemplate.send("preset-response-topic", presetDTO);
    }
}