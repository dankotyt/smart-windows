package com.junit5.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.kafka.core.KafkaTemplate;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.entity.Preset;
import ru.pin36bik.exceptions.PresetNotFoundException;
import ru.pin36bik.repository.PresetRepository;
import ru.pin36bik.service.PresetService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableCaching
class PresetServiceTest {

    @Mock
    private PresetRepository presetRepos;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PresetService presetService;

    private CacheManager createCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Cache presetsCache = new ConcurrentMapCache("presets");
        cacheManager.setCaches(Collections.singletonList(presetsCache));
        return cacheManager;
    }

    @Test
    void getPresetById_ShouldReturnPresetDTO_WhenPresetExists() {
        Long id = 1L;
        Preset preset = new Preset();
        preset.setId(id);
        preset.setPresetName("Test Preset");

        PresetDTO expectedDto = new PresetDTO();
        expectedDto.setId(id);
        expectedDto.setPresetName("Test Preset");

        when(presetRepos.findById(id)).thenReturn(Optional.of(preset));
        when(modelMapper.map(preset, PresetDTO.class)).thenReturn(expectedDto);

        PresetDTO result = presetService.getPresetById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test Preset", result.getPresetName());
        verify(kafkaTemplate).send("preset-download-topic", "Test Preset");
        verify(presetRepos, times(1)).findById(id);
    }

    @Test
    void getPresetById_ShouldThrowException_WhenPresetNotExists() {
        Long id = 99L;
        when(presetRepos.findById(id)).thenReturn(Optional.empty());

        assertThrows(PresetNotFoundException.class, () -> {
            presetService.getPresetById(id);
        });
        verify(presetRepos, times(1)).findById(id);
        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void createPreset_ShouldSaveAndReturnDTO() {
        PresetDTO inputDto = new PresetDTO();
        inputDto.setPresetName("New Preset");

        Preset preset = new Preset();
        preset.setPresetName("New Preset");

        Preset savedPreset = new Preset();
        savedPreset.setId(1L);
        savedPreset.setPresetName("New Preset");

        PresetDTO expectedDto = new PresetDTO();
        expectedDto.setId(1L);
        expectedDto.setPresetName("New Preset");

        when(modelMapper.map(inputDto, Preset.class)).thenReturn(preset);
        when(presetRepos.save(preset)).thenReturn(savedPreset);
        when(modelMapper.map(savedPreset, PresetDTO.class)).thenReturn(expectedDto);

        PresetDTO result = presetService.createPreset(inputDto);

        assertNotNull(result.getId());
        assertEquals("New Preset", result.getPresetName());
        verify(presetRepos, times(1)).save(preset);
    }

    @Test
    void updatePreset_ShouldUpdateExistingPreset() {
        Long id = 1L;
        PresetDTO inputDto = new PresetDTO();
        inputDto.setId(id);
        inputDto.setPresetName("Updated Preset");

        Preset existingPreset = new Preset();
        existingPreset.setId(id);
        existingPreset.setPresetName("Old Preset");

        Preset updatedPreset = new Preset();
        updatedPreset.setId(id);
        updatedPreset.setPresetName("Updated Preset");

        PresetDTO expectedDto = new PresetDTO();
        expectedDto.setId(id);
        expectedDto.setPresetName("Updated Preset");

        when(presetRepos.findById(id)).thenReturn(Optional.of(existingPreset));
        doNothing().when(modelMapper).map(inputDto, existingPreset);
        when(presetRepos.save(existingPreset)).thenReturn(updatedPreset);
        when(modelMapper.map(updatedPreset, PresetDTO.class)).thenReturn(expectedDto);

        PresetDTO result = presetService.updatePreset(inputDto);

        assertEquals(id, result.getId());
        assertEquals("Updated Preset", result.getPresetName());
        verify(presetRepos, times(1)).findById(id);
        verify(presetRepos, times(1)).save(existingPreset);
    }

    @Test
    void updatePreset_ShouldThrowException_WhenPresetNotExists() {
        Long id = 99L;
        PresetDTO inputDto = new PresetDTO();
        inputDto.setId(id);

        when(presetRepos.findById(id)).thenReturn(Optional.empty());

        assertThrows(PresetNotFoundException.class, () -> {
            presetService.updatePreset(inputDto);
        });
        verify(presetRepos, times(1)).findById(id);
        verify(presetRepos, never()).save(any());
    }

    @Test
    void deletePreset_ShouldDelete_WhenPresetExists() {
        Long id = 1L;
        when(presetRepos.existsById(id)).thenReturn(true);

        presetService.deletePreset(id);

        verify(presetRepos, times(1)).existsById(id);
        verify(presetRepos, times(1)).deleteById(id);
    }

    @Test
    void deletePreset_ShouldThrowException_WhenPresetNotExists() {
        Long id = 99L;
        when(presetRepos.existsById(id)).thenReturn(false);

        assertThrows(PresetNotFoundException.class, () -> {
            presetService.deletePreset(id);
        });
        verify(presetRepos, times(1)).existsById(id);
        verify(presetRepos, never()).deleteById(any());
    }
}