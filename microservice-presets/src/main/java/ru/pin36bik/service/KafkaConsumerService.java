package ru.pin36bik.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.entity.Preset;
import ru.pin36bik.repository.PresetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

@Service
public class KafkaConsumerService {

    private final PresetRepository presetRepository;
    private final KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @Autowired
    public KafkaConsumerService(PresetRepository presetRepository, KafkaTemplate<String, PresetDTO> kafkaTemplate) {
        this.presetRepository = presetRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "preset-request-topic", groupId = "preset-group")
    public void handlePresetRequest(String presetName) {
        PresetDTO presetDTO = getPresetByName(presetName);
        kafkaTemplate.send("preset-response-topic", presetDTO);
    }

    @Cacheable(value = "presets", key = "#presetName")
    public PresetDTO getPresetByName(String presetName) {
        Preset preset = presetRepository.findByPresetName(presetName)
                .orElseThrow(() -> new RuntimeException("Preset not found with name: " + presetName));
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