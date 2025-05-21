package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.entity.Preset;
import ru.pin36bik.exceptions.PresetNotFoundException;
import ru.pin36bik.repository.PresetRepository;

@Service
@RequiredArgsConstructor
public class PresetService {

    private final PresetRepository presetRepos;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ModelMapper modelMapper;

    @Cacheable(value = "presets", key = "#id")
    public PresetDTO getPresetById(final Long id) {
        Preset preset = presetRepos.findById(id)
                .orElseThrow(() ->
                        new PresetNotFoundException(
                                "Пресет не найден по идентификатору:" + id));
        kafkaTemplate.send("preset-download-topic", preset.getPresetName());
        System.out.println("Sent preset name " + preset.getPresetName());
        return modelMapper.map(preset, PresetDTO.class);
    }

    @CachePut(value = "presets", key = "#presetDTO.id")
    public PresetDTO createPreset(final PresetDTO presetDTO) {
        Preset preset = modelMapper.map(presetDTO, Preset.class);
        Preset savedPreset = presetRepos.save(preset);
        return modelMapper.map(savedPreset, PresetDTO.class);
    }

    @CachePut(value = "presets", key = "#presetDTO.id")
    public PresetDTO updatePreset(
            final PresetDTO presetDTO) {
        Preset preset = presetRepos.findById(presetDTO.getId())
                .orElseThrow(() -> new PresetNotFoundException(
                        "Пресет не найден по идентификатору: "
                                + presetDTO.getId()));
        modelMapper.map(presetDTO, preset);
        Preset updatedPreset = presetRepos.save(preset);
        return modelMapper.map(updatedPreset, PresetDTO.class);
    }

    @CacheEvict(value = "presets", key = "#id")
    public void deletePreset(final Long id) {
        if (!presetRepos.existsById(id)) {
            throw new PresetNotFoundException(
                    "Пресет не найден по идентификатору: " + id);
        }
        presetRepos.deleteById(id);
    }
}
