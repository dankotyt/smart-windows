package com.junit5.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.entity.Preset;
import ru.pin36bik.repository.PresetRepository;
import ru.pin36bik.service.KafkaConsumerService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceTest {

    @Mock
    private PresetRepository presetRepository;

    @Mock
    private KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    private Preset preset;
    private PresetDTO presetDTO;

    @BeforeEach
    public void setUp() {
        preset = new Preset();
        preset.setId(1L);
        preset.setPresetName("Test Preset");
        preset.setVentilationFlag(true);
        preset.setVentilationTimer(30);
        preset.setDarknessValue(50);
        preset.setFrameColor(20004567);

        presetDTO = new PresetDTO();
        presetDTO.setId(1L);
        presetDTO.setPresetName("Test Preset");
        presetDTO.setVentilationFlag(true);
        presetDTO.setVentilationTimer(30);
        presetDTO.setDarknessValue(50);
        presetDTO.setFrameColor(20004567);
    }

    @Test
    public void testGetPresetByName_Success() {
        when(presetRepository.findByPresetName("Test Preset")).thenReturn(Optional.of(preset));

        PresetDTO result = kafkaConsumerService.getPresetByName("Test Preset");

        assertNotNull(result);
        assertEquals(presetDTO.getId(), result.getId());
        assertEquals(presetDTO.getPresetName(), result.getPresetName());
        assertEquals(presetDTO.getVentilationFlag(), result.getVentilationFlag());
        assertEquals(presetDTO.getVentilationTimer(), result.getVentilationTimer());
        assertEquals(presetDTO.getDarknessValue(), result.getDarknessValue());
        assertEquals(presetDTO.getFrameColor(), result.getFrameColor());

        verify(presetRepository, times(1)).findByPresetName("Test Preset");
    }

    @Test
    public void testGetPresetByName_NotFound() {
        when(presetRepository.findByPresetName("Unknown Preset")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kafkaConsumerService.getPresetByName("Unknown Preset");
        });

        assertEquals("Preset not found with name: Unknown Preset", exception.getMessage());

        verify(presetRepository, times(1)).findByPresetName("Unknown Preset");
    }

    @Test
    public void testHandlePresetRequest_Success() {
        when(presetRepository.findByPresetName("Test Preset")).thenReturn(Optional.of(preset));

        kafkaConsumerService.handlePresetRequest("Test Preset");

        verify(presetRepository, times(1)).findByPresetName("Test Preset");
        verify(kafkaTemplate, times(1)).send(eq("preset-response-topic"), any(PresetDTO.class));
    }

    @Test
    public void testHandlePresetRequest_PresetNotFound() {
        when(presetRepository.findByPresetName("Unknown Preset")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kafkaConsumerService.handlePresetRequest("Unknown Preset");
        });

        assertEquals("Preset not found with name: Unknown Preset", exception.getMessage());

        verify(presetRepository, times(1)).findByPresetName("Unknown Preset");
        verify(kafkaTemplate, never()).send(anyString(), any(PresetDTO.class));
    }
}