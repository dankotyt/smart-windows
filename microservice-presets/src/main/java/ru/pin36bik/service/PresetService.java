package ru.pin36bik.service;

import org.springframework.cache.annotation.CachePut;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.entity.Preset;
import ru.pin36bik.repository.PresetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PresetService {

    private final PresetRepository presetRepos;
    private final KafkaTemplate<String, PresetDTO> kafkaTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public PresetService(PresetRepository presetRepos, KafkaTemplate<String, PresetDTO> kafkaTemplate, ModelMapper modelMapper) {
        this.presetRepos = presetRepos;
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
    }

    @Cacheable(value = "presets", key = "#id")
    public PresetDTO getPresetById(Long id) {
        Preset preset = presetRepos.findById(id)
                .orElseThrow(() -> new RuntimeException("Preset not found with id: " + id));
        return modelMapper.map(preset, PresetDTO.class);
    }

    @CachePut(value = "presets", key = "#presetDTO.id")
    public PresetDTO createPreset(PresetDTO presetDTO) {
        Preset preset = modelMapper.map(presetDTO, Preset.class);
        Preset savedPreset = presetRepos.save(preset);
        kafkaTemplate.send("preset-topic", modelMapper.map(savedPreset, PresetDTO.class));
        return modelMapper.map(savedPreset, PresetDTO.class);
    }

    @CachePut(value = "presets", key = "#presetDTO.id")
    public PresetDTO updatePreset(PresetDTO presetDTO) {
        Preset preset = presetRepos.findById(presetDTO.getId())
                .orElseThrow(() -> new RuntimeException("Preset not found with id: " + presetDTO.getId()));
        modelMapper.map(presetDTO, preset);
        Preset updatedPreset = presetRepos.save(preset);
        kafkaTemplate.send("preset-topic", modelMapper.map(updatedPreset, PresetDTO.class));
        return modelMapper.map(updatedPreset, PresetDTO.class);
    }

    @CacheEvict(value = "presets", key = "#id")
    public void deletePreset(Long id) {
        presetRepos.deleteById(id);
    }
}