package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.entity.Preset;
import ru.pin36bik.repository.PresetRepository;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final PresetRepository presetRepository;
    private final KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @KafkaListener(topics = "preset-request-topic",
            groupId = "test-consumer-group")
    public void handlePresetRequest(final String presetName) {
        PresetDTO presetDTO = getPresetByName(presetName);
        System.out.println("Received preset: " + presetDTO);
        kafkaTemplate.send("preset-response-topic", presetDTO);
    }

    @Cacheable(value = "presets", key = "#presetName")
    public PresetDTO getPresetByName(final String presetName) {
        Preset preset = presetRepository.findByPresetName(presetName)
                .orElseThrow(() -> new RuntimeException(
                        "Preset not found with name: " + presetName));
        return new PresetDTO(
                preset.getId(),
                preset.getPresetName(),
                preset.getVentilationFlag(),
                preset.getVentilationTimer(),
                preset.getDarknessValue(),
                preset.getFrameColor()
        );
    }
}
