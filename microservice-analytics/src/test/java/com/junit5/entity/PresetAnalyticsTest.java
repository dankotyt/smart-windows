package com.junit5.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pin36bik.entity.PresetAnalytics;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PresetAnalyticsTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreatePresetAnalyticsWithAllFields() {
        LocalDateTime createdAt = LocalDateTime.parse("2023-12-01T10:15:30");

        PresetAnalytics entity = new PresetAnalytics();
        entity.setId(12345L);
        entity.setDownloadsNumber(456000L);
        entity.setPresetName("Morning Mode");
        entity.setCreatedAt(createdAt);

        assertThat(entity.getId()).isEqualTo(12345L);
        assertThat(entity.getDownloadsNumber()).isEqualTo(456000L);
        assertThat(entity.getPresetName()).isEqualTo("Morning Mode");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldCreateEmptyPresetAnalytics() {
        PresetAnalytics entity = new PresetAnalytics();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getDownloadsNumber()).isNull();
        assertThat(entity.getPresetName()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
    }

    @Test
    void shouldSerializeToJsonWithDefaultFieldNames() throws JsonProcessingException {
        PresetAnalytics entity = new PresetAnalytics();
        entity.setId(12345L);
        entity.setDownloadsNumber(456000L);
        entity.setPresetName("Morning Mode");
        entity.setCreatedAt(LocalDateTime.parse("2023-12-01T10:15:30"));

        String json = objectMapper.writeValueAsString(entity);

        assertThat(json).contains("\"id\":12345");
        assertThat(json).contains("\"downloadsNumber\":456000");
        assertThat(json).contains("\"presetName\":\"Morning Mode\"");
        assertThat(json).contains("\"createdAt\":\"2023-12-01T10:15:30\"");
    }

    @Test
    void shouldDeserializeFromJsonWithDefaultFieldNames() throws JsonProcessingException {
        String json = """
            {
                "id": 12345,
                "downloadsNumber": 456000,
                "presetName": "Morning Mode",
                "createdAt": "2023-12-01T10:15:30"
            }
            """;

        PresetAnalytics entity = objectMapper.readValue(json, PresetAnalytics.class);

        assertThat(entity.getId()).isEqualTo(12345L);
        assertThat(entity.getDownloadsNumber()).isEqualTo(456000L);
        assertThat(entity.getPresetName()).isEqualTo("Morning Mode");
        assertThat(entity.getCreatedAt()).isEqualTo(LocalDateTime.parse("2023-12-01T10:15:30"));
    }

    @Test
    void shouldHandleNullFieldsDuringSerialization() throws JsonProcessingException {
        PresetAnalytics entity = new PresetAnalytics();

        String json = objectMapper.writeValueAsString(entity);

        assertThat(json).contains("\"id\":null");
        assertThat(json).contains("\"downloadsNumber\":null");
        assertThat(json).contains("\"presetName\":null");
        assertThat(json).contains("\"createdAt\":null");
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        LocalDateTime createdAt = LocalDateTime.parse("2023-12-01T10:15:30");

        PresetAnalytics entity1 = new PresetAnalytics();
        entity1.setId(12345L);
        entity1.setDownloadsNumber(456000L);
        entity1.setPresetName("Morning Mode");
        entity1.setCreatedAt(createdAt);

        PresetAnalytics entity2 = new PresetAnalytics();
        entity2.setId(12345L);
        entity2.setDownloadsNumber(456000L);
        entity2.setPresetName("Morning Mode");
        entity2.setCreatedAt(createdAt);

        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenFieldsDiffer() {
        LocalDateTime createdAt = LocalDateTime.parse("2023-12-01T10:15:30");

        PresetAnalytics entity1 = new PresetAnalytics();
        entity1.setId(12345L);
        entity1.setDownloadsNumber(456000L);
        entity1.setPresetName("Morning Mode");
        entity1.setCreatedAt(createdAt);

        PresetAnalytics entity2 = new PresetAnalytics();
        entity2.setId(12346L); // Разный ID
        entity2.setDownloadsNumber(456000L);
        entity2.setPresetName("Morning Mode");
        entity2.setCreatedAt(createdAt);

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    void shouldReturnCorrectToString() {
        PresetAnalytics entity = new PresetAnalytics();
        entity.setId(12345L);
        entity.setDownloadsNumber(456000L);
        entity.setPresetName("Morning Mode");
        entity.setCreatedAt(LocalDateTime.parse("2023-12-01T10:15:30"));

        String toString = entity.toString();

        assertThat(toString).contains("id=12345");
        assertThat(toString).contains("downloadsNumber=456000");
        assertThat(toString).contains("presetName=Morning Mode");
        assertThat(toString).contains("createdAt=2023-12-01T10:15:30");
    }
}
