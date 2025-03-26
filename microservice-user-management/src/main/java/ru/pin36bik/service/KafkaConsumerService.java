package ru.pin36bik.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.PresetDTO;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "preset-response-topic", groupId = "user-group")
    public void handlePresetResponse(PresetDTO presetDTO) {
        System.out.println("Received preset: " + presetDTO);
        // Логика обработки полученного пресета
    }
}