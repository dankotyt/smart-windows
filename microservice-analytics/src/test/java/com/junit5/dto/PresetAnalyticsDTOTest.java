package com.junit5.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pin36bik.dto.PresetAnalyticsDTO;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PresetAnalyticsDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreatePresetAnalyticsDTOWithAllFields() {
        LocalDateTime createdAt = LocalDateTime.parse("2023-12-01T10:15:30");

        PresetAnalyticsDTO dto = new PresetAnalyticsDTO(
                12345L,
                456000L,
                "Morning Mode",
                createdAt
        );

        assertThat(dto.getId()).isEqualTo(12345L);
        assertThat(dto.getDownloadsNumber()).isEqualTo(456000L);
        assertThat(dto.getPresetName()).isEqualTo("Morning Mode");
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldCreateEmptyPresetAnalyticsDTO() {
        PresetAnalyticsDTO dto = new PresetAnalyticsDTO();

        assertThat(dto.getId()).isNull();
        assertThat(dto.getDownloadsNumber()).isNull();
        assertThat(dto.getPresetName()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
    }

    @Test
    void shouldSerializeToJsonWithCorrectFieldNames() throws JsonProcessingException {
        PresetAnalyticsDTO dto = new PresetAnalyticsDTO(
                12345L,
                456000L,
                "Morning Mode",
                LocalDateTime.parse("2023-12-01T10:15:30")
        );

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":12345");
        assertThat(json).contains("\"downloads_number\":456000");
        assertThat(json).contains("\"preset_name\":\"Morning Mode\"");
        assertThat(json).contains("\"created_at\":\"2023-12-01T10:15:30\"");
    }

    @Test
    void shouldDeserializeFromJsonWithCorrectFieldNames() throws JsonProcessingException {
        String json = """
            {
                "id": 12345,
                "downloads_number": 456000,
                "preset_name": "Morning Mode",
                "created_at": "2023-12-01T10:15:30"
            }
            """;

        PresetAnalyticsDTO dto = objectMapper.readValue(json, PresetAnalyticsDTO.class);

        assertThat(dto.getId()).isEqualTo(12345L);
        assertThat(dto.getDownloadsNumber()).isEqualTo(456000L);
        assertThat(dto.getPresetName()).isEqualTo("Morning Mode");
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDateTime.parse("2023-12-01T10:15:30"));
    }

    @Test
    void shouldHandleNullFieldsDuringSerialization() throws JsonProcessingException {
        PresetAnalyticsDTO dto = new PresetAnalyticsDTO();

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":null");
        assertThat(json).contains("\"downloads_number\":null");
        assertThat(json).contains("\"preset_name\":null");
        assertThat(json).contains("\"created_at\":null");
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        LocalDateTime createdAt = LocalDateTime.now();
        PresetAnalyticsDTO dto1 = new PresetAnalyticsDTO(1L, 10L, "Test", createdAt);
        PresetAnalyticsDTO dto2 = new PresetAnalyticsDTO(1L, 10L, "Test", createdAt);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenFieldsDiffer() {
        LocalDateTime now = LocalDateTime.now();
        PresetAnalyticsDTO dto1 = new PresetAnalyticsDTO(1L, 10L, "Test", now);
        PresetAnalyticsDTO dto2 = new PresetAnalyticsDTO(2L, 10L, "Test", now);

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void shouldReturnCorrectToString() {
        PresetAnalyticsDTO dto = new PresetAnalyticsDTO(
                12345L,
                456000L,
                "Morning Mode",
                LocalDateTime.parse("2023-12-01T10:15:30")
        );

        String toString = dto.toString();

        assertThat(toString).contains("id=12345");
        assertThat(toString).contains("downloadsNumber=456000");
        assertThat(toString).contains("presetName=Morning Mode");
        assertThat(toString).contains("createdAt=2023-12-01T10:15:30");
    }
}
