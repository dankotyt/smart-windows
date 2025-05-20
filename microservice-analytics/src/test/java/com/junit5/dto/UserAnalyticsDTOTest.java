package com.junit5.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pin36bik.dto.UserAnalyticsDTO;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserAnalyticsDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateUserAnalyticsDTOWithAllFields() {
        LocalDateTime lastLogin = LocalDateTime.parse("2023-12-01T10:15:30");
        LocalDateTime timestamp = LocalDateTime.parse("2023-12-01T10:20:00");

        UserAnalyticsDTO dto = new UserAnalyticsDTO(
                456L,
                5,
                lastLogin,
                timestamp
        );

        assertThat(dto.getUserId()).isEqualTo(456L);
        assertThat(dto.getLoginCount()).isEqualTo(5);
        assertThat(dto.getLastLogin()).isEqualTo(lastLogin);
        assertThat(dto.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldCreateEmptyUserAnalyticsDTO() {
        UserAnalyticsDTO dto = new UserAnalyticsDTO();

        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getLoginCount()).isEqualTo(0);
        assertThat(dto.getLastLogin()).isNull();
        assertThat(dto.getTimestamp()).isNull();
    }

    @Test
    void shouldSerializeToJsonWithCorrectFieldNames() throws JsonProcessingException {
        UserAnalyticsDTO dto = new UserAnalyticsDTO(
                456L,
                5,
                LocalDateTime.parse("2023-12-01T10:15:30"),
                LocalDateTime.parse("2023-12-01T10:20:00")
        );

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"user_id\":456");
        assertThat(json).contains("\"login_count\":5");
        assertThat(json).contains("\"last_login\":\"2023-12-01T10:15:30\"");
        assertThat(json).contains("\"timestamp\":\"2023-12-01T10:20:00\"");
    }

    @Test
    void shouldDeserializeFromJsonWithCorrectFieldNames() throws JsonProcessingException {
        String json = """
            {
                "user_id": 456,
                "login_count": 5,
                "last_login": "2023-12-01T10:15:30",
                "timestamp": "2023-12-01T10:20:00"
            }
            """;

        UserAnalyticsDTO dto = objectMapper.readValue(json, UserAnalyticsDTO.class);

        assertThat(dto.getUserId()).isEqualTo(456L);
        assertThat(dto.getLoginCount()).isEqualTo(5);
        assertThat(dto.getLastLogin()).isEqualTo(LocalDateTime.parse("2023-12-01T10:15:30"));
        assertThat(dto.getTimestamp()).isEqualTo(LocalDateTime.parse("2023-12-01T10:20:00"));
    }

    @Test
    void shouldHandleNullFieldsDuringSerialization() throws JsonProcessingException {
        UserAnalyticsDTO dto = new UserAnalyticsDTO();

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"user_id\":null");
        assertThat(json).contains("\"login_count\":0");
        assertThat(json).contains("\"last_login\":null");
        assertThat(json).contains("\"timestamp\":null");
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        LocalDateTime lastLogin = LocalDateTime.parse("2023-12-01T10:15:30");
        LocalDateTime timestamp = LocalDateTime.parse("2023-12-01T10:20:00");

        UserAnalyticsDTO dto1 = new UserAnalyticsDTO(456L, 5, lastLogin, timestamp);
        UserAnalyticsDTO dto2 = new UserAnalyticsDTO(456L, 5, lastLogin, timestamp);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenFieldsDiffer() {
        LocalDateTime lastLogin = LocalDateTime.parse("2023-12-01T10:15:30");
        LocalDateTime timestamp = LocalDateTime.parse("2023-12-01T10:20:00");

        UserAnalyticsDTO dto1 = new UserAnalyticsDTO(456L, 5, lastLogin, timestamp);
        UserAnalyticsDTO dto2 = new UserAnalyticsDTO(457L, 5, lastLogin, timestamp);

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void shouldReturnCorrectToString() {
        UserAnalyticsDTO dto = new UserAnalyticsDTO(
                456L,
                5,
                LocalDateTime.parse("2023-12-01T10:15:30"),
                LocalDateTime.parse("2023-12-01T10:20:00")
        );

        String toString = dto.toString();

        assertThat(toString).contains("userId=456");
        assertThat(toString).contains("loginCount=5");
        assertThat(toString).contains("lastLogin=2023-12-01T10:15:30");
        assertThat(toString).contains("timestamp=2023-12-01T10:20:00");
    }
}
