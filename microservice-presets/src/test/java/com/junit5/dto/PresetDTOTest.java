package com.junit5.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.pin36bik.dto.PresetDTO;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PresetDTOTest {

    private static Validator validator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidDto() {
        PresetDTO dto = new PresetDTO(
                1L,
                "Default Preset",
                true,
                600,
                75,
                16777215
        );

        assertAll(
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals("Default Preset", dto.getPresetName()),
                () -> assertTrue(dto.getVentilationFlag()),
                () -> assertEquals(600, dto.getVentilationTimer()),
                () -> assertEquals(75, dto.getDarknessValue()),
                () -> assertEquals(16777215, dto.getFrameColor())
        );
    }

    @Test
    void shouldValidateConstraints() {
        PresetDTO invalidDto = new PresetDTO();
        invalidDto.setPresetName(""); // @NotBlank
        invalidDto.setVentilationFlag(null); // @NotNull
        invalidDto.setVentilationTimer(100000); // @Max(86400)
        invalidDto.setDarknessValue(-10); // @Min(0)
        invalidDto.setFrameColor(null); // @NotNull

        Set<String> violations = validator.validate(invalidDto).stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertAll(
                () -> assertTrue(violations.contains("presetName")),
                () -> assertTrue(violations.contains("ventilationFlag")),
                () -> assertTrue(violations.contains("ventilationTimer")),
                () -> assertTrue(violations.contains("darknessValue")),
                () -> assertTrue(violations.contains("frameColor"))
        );
    }

    @Test
    void shouldSerializeToJson() throws JsonProcessingException {
        PresetDTO dto = new PresetDTO(
                1L,
                "Night Mode",
                false,
                300,
                90,
                0x000000
        );

        String json = objectMapper.writeValueAsString(dto);

        assertAll(
                () -> assertTrue(json.contains("\"id\":1")),
                () -> assertTrue(json.contains("\"preset_name\":\"Night Mode\"")),
                () -> assertTrue(json.contains("\"ventilation_flag\":false")),
                () -> assertTrue(json.contains("\"ventilation_timer\":300")),
                () -> assertTrue(json.contains("\"darkness_value\":90")),
                () -> assertTrue(json.contains("\"frame_color\":0"))
        );
    }

    @Test
    void shouldDeserializeFromJson() throws JsonProcessingException {
        String json = """
        {
            "id": 2,
            "preset_name": "At Dawn",
            "ventilation_flag": true,
            "ventilation_timer": 1200,
            "darkness_value": 20,
            "frame_color": 16711680
        }
        """;

        PresetDTO dto = objectMapper.readValue(json, PresetDTO.class);

        assertAll(
                () -> assertEquals(2L, dto.getId()),
                () -> assertEquals("At Dawn", dto.getPresetName()),
                () -> assertTrue(dto.getVentilationFlag()),
                () -> assertEquals(1200, dto.getVentilationTimer()),
                () -> assertEquals(20, dto.getDarknessValue()),
                () -> assertEquals(16711680, dto.getFrameColor())
        );
    }

    @Test
    void shouldHandleNullValues() {
        PresetDTO dto = new PresetDTO();
        dto.setId(null);
        dto.setPresetName(null);

        Set<String> violations = validator.validate(dto).stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertAll(
                () -> assertTrue(violations.contains("presetName")), // @NotBlank
                () -> assertFalse(violations.contains("id")) // ID может быть null
        );
    }
}