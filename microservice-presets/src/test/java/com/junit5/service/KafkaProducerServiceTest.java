package com.junit5.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.service.KafkaProducerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    private PresetDTO presetDTO;

    @BeforeEach
    public void setUp() {
        presetDTO = new PresetDTO();
        presetDTO.setId(1L);
        presetDTO.setPresetName("TestPreset");
        presetDTO.setVentilationFlag(true);
        presetDTO.setVentilationTimer(30);
        presetDTO.setDarknessValue(50);
        presetDTO.setFrameColor(20004567);
    }

    @Test
    public void testSendPresetResponse_Success() {
        kafkaProducerService.sendPresetResponse(presetDTO);

        ArgumentCaptor<PresetDTO> captor = ArgumentCaptor.forClass(PresetDTO.class);
        verify(kafkaTemplate, times(1)).send(eq("preset-response-topic"), captor.capture());

        PresetDTO sentPresetDTO = captor.getValue();
        assertNotNull(sentPresetDTO);
        assertEquals(presetDTO.getId(), sentPresetDTO.getId());
        assertEquals(presetDTO.getPresetName(), sentPresetDTO.getPresetName());
        assertEquals(presetDTO.getVentilationFlag(), sentPresetDTO.getVentilationFlag());
        assertEquals(presetDTO.getVentilationTimer(), sentPresetDTO.getVentilationTimer());
        assertEquals(presetDTO.getDarknessValue(), sentPresetDTO.getDarknessValue());
        assertEquals(presetDTO.getFrameColor(), sentPresetDTO.getFrameColor());
    }

    @Test
    public void testSendPresetResponse_Failure() {
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(eq("preset-response-topic"), any(PresetDTO.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kafkaProducerService.sendPresetResponse(presetDTO);
        });

        assertEquals("Failed to send preset to Kafka", exception.getMessage());
    }
}