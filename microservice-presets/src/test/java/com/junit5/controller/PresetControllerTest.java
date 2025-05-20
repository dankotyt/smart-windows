package com.junit5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.pin36bik.controller.PresetController;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.exceptions.PresetNotFoundException;
import ru.pin36bik.service.PresetService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PresetControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private PresetService presetService;

    @Mock
    private KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @InjectMocks
    private PresetController presetController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(presetController).build();
    }

    @Test
    void getPresetById_ShouldReturnPreset() throws Exception {
        PresetDTO mockPreset = new PresetDTO();
        mockPreset.setId(1L);
        mockPreset.setPresetName("Test Preset");

        when(presetService.getPresetById(1L)).thenReturn(mockPreset);

        mockMvc.perform(get("/api/presets/v0/get-by-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.preset_name").value("Test Preset"));

        verify(presetService, times(1)).getPresetById(1L);
    }

    @Test
    void getPresetById_ShouldReturn404WhenNotFound() throws Exception {
        when(presetService.getPresetById(1L)).thenThrow(new PresetNotFoundException("Пресет не найден по идентификатору: 1"));

        mockMvc.perform(get("/api/presets/v0/get-by-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(presetService, times(1)).getPresetById(1L);
    }

    @Test
    void createPreset_ShouldCreateAndReturnPreset() throws Exception {
        PresetDTO inputDto = new PresetDTO();
        inputDto.setPresetName("New Preset");
        inputDto.setVentilationFlag(true);
        inputDto.setVentilationTimer(100);
        inputDto.setDarknessValue(50);
        inputDto.setFrameColor(123456);

        PresetDTO outputDto = new PresetDTO();
        outputDto.setId(1L);
        outputDto.setPresetName("New Preset");
        outputDto.setVentilationFlag(true);
        outputDto.setVentilationTimer(100);
        outputDto.setDarknessValue(50);
        outputDto.setFrameColor(123456);

        when(presetService.createPreset(any(PresetDTO.class))).thenReturn(outputDto);

        mockMvc.perform(post("/api/presets/v0/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.preset_name").value("New Preset"))
                .andExpect(jsonPath("$.ventilation_flag").value(true))
                .andExpect(jsonPath("$.ventilation_timer").value(100))
                .andExpect(jsonPath("$.darkness_value").value(50))
                .andExpect(jsonPath("$.frame_color").value(123456));

        verify(presetService, times(1)).createPreset(any(PresetDTO.class));
    }

    @Test
    void createPreset_ShouldReturn400ForInvalidInput() throws Exception {
        PresetDTO invalidDto = new PresetDTO();

        mockMvc.perform(post("/api/presets/v0/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePresetById_ShouldUpdateAndReturnPreset() throws Exception {
        PresetDTO inputDto = new PresetDTO();
        inputDto.setId(1L);
        inputDto.setPresetName("Updated Preset");
        inputDto.setVentilationFlag(true);
        inputDto.setVentilationTimer(100);
        inputDto.setDarknessValue(50);
        inputDto.setFrameColor(123456);

        when(presetService.updatePreset(any(PresetDTO.class))).thenReturn(inputDto);

        mockMvc.perform(put("/api/presets/v0/update-by-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.preset_name").value("Updated Preset"))
                .andExpect(jsonPath("$.ventilation_flag").value(true))
                .andExpect(jsonPath("$.ventilation_timer").value(100))
                .andExpect(jsonPath("$.darkness_value").value(50))
                .andExpect(jsonPath("$.frame_color").value(123456));

        verify(presetService, times(1)).updatePreset(any(PresetDTO.class));
    }

    @Test
    void updatePresetById_ShouldReturn404WhenNotFound() throws Exception {
        PresetDTO inputDto = new PresetDTO();
        inputDto.setId(100L);
        inputDto.setPresetName("Updated Preset");
        inputDto.setVentilationFlag(true);
        inputDto.setVentilationTimer(100);
        inputDto.setDarknessValue(50);
        inputDto.setFrameColor(123456);

        when(presetService.updatePreset(any(PresetDTO.class)))
                .thenThrow(new PresetNotFoundException("Пресет не найден по идентификатору: 100"));

        mockMvc.perform(put("/api/presets/v0/update-by-id/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isNotFound());

        verify(presetService, times(1)).updatePreset(any(PresetDTO.class));
    }

    @Test
    void deletePresetById_ShouldReturn204() throws Exception {
        doNothing().when(presetService).deletePreset(1L);

        mockMvc.perform(delete("/api/presets/v0/delete-by-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(presetService, times(1)).deletePreset(1L);
    }

    @Test
    void deletePresetById_ShouldReturn404WhenNotFound() throws Exception {
        doThrow(new PresetNotFoundException("Пресет не найден по идентификатору: 100")).when(presetService).deletePreset(100L);

        mockMvc.perform(delete("/api/presets/v0/delete-by-id/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(presetService, times(1)).deletePreset(100L);
    }
}