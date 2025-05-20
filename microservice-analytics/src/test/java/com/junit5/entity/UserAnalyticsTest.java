package com.junit5.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pin36bik.entity.UserAnalytics;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserAnalyticsTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateUserAnalyticsWithAllFields() {
        LocalDateTime lastLogin = LocalDateTime.parse("2023-12-01T10:15:30");
        LocalDateTime timestamp = LocalDateTime.parse("2023-12-01T10:20:00");

        UserAnalytics entity = new UserAnalytics();
        entity.setUserId(456L);
        entity.setLoginCount(5);
        entity.setLastLogin(lastLogin);
        entity.setTimestamp(timestamp);

        assertThat(entity.getUserId()).isEqualTo(456L);
        assertThat(entity.getLoginCount()).isEqualTo(5);
        assertThat(entity.getLastLogin()).isEqualTo(lastLogin);
        assertThat(entity.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldCreateEmptyUserAnalytics() {
        UserAnalytics entity = new UserAnalytics();

        assertThat(entity.getUserId()).isNull();
        assertThat(entity.getLoginCount()).isEqualTo(0);
        assertThat(entity.getLastLogin()).isNull();
        assertThat(entity.getTimestamp()).isNull();
    }

    @Test
    void shouldSerializeToJsonWithDefaultFieldNames() throws JsonProcessingException {
        UserAnalytics entity = new UserAnalytics();
        entity.setUserId(456L);
        entity.setLoginCount(5);
        entity.setLastLogin(LocalDateTime.parse("2023-12-01T10:15:30"));
        entity.setTimestamp(LocalDateTime.parse("2023-12-01T10:20:00"));

        String json = objectMapper.writeValueAsString(entity);

        assertThat(json).contains("\"userId\":456");
        assertThat(json).contains("\"loginCount\":5");
        assertThat(json).contains("\"lastLogin\":\"2023-12-01T10:15:30\"");
        assertThat(json).contains("\"timestamp\":\"2023-12-01T10:20:00\"");
    }

    @Test
    void shouldDeserializeFromJsonWithDefaultFieldNames() throws JsonProcessingException {
        String json = """
            {
                "userId": 456,
                "loginCount": 5,
                "lastLogin": "2023-12-01T10:15:30",
                "timestamp": "2023-12-01T10:20:00"
            }
            """;

        UserAnalytics entity = objectMapper.readValue(json, UserAnalytics.class);

        assertThat(entity.getUserId()).isEqualTo(456L);
        assertThat(entity.getLoginCount()).isEqualTo(5);
        assertThat(entity.getLastLogin()).isEqualTo(LocalDateTime.parse("2023-12-01T10:15:30"));
        assertThat(entity.getTimestamp()).isEqualTo(LocalDateTime.parse("2023-12-01T10:20:00"));
    }

    @Test
    void shouldHandleNullFieldsDuringSerialization() throws JsonProcessingException {
        UserAnalytics entity = new UserAnalytics();

        String json = objectMapper.writeValueAsString(entity);

        assertThat(json).contains("\"userId\":null");
        assertThat(json).contains("\"loginCount\":0");
        assertThat(json).contains("\"lastLogin\":null");
        assertThat(json).contains("\"timestamp\":null");
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        LocalDateTime lastLogin = LocalDateTime.parse("2023-12-01T10:15:30");
        LocalDateTime timestamp = LocalDateTime.parse("2023-12-01T10:20:00");

        UserAnalytics entity1 = new UserAnalytics();
        entity1.setUserId(456L);
        entity1.setLoginCount(5);
        entity1.setLastLogin(lastLogin);
        entity1.setTimestamp(timestamp);

        UserAnalytics entity2 = new UserAnalytics();
        entity2.setUserId(456L);
        entity2.setLoginCount(5);
        entity2.setLastLogin(lastLogin);
        entity2.setTimestamp(timestamp);

        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenFieldsDiffer() {
        LocalDateTime lastLogin = LocalDateTime.parse("2023-12-01T10:15:30");
        LocalDateTime timestamp = LocalDateTime.parse("2023-12-01T10:20:00");

        UserAnalytics entity1 = new UserAnalytics();
        entity1.setUserId(456L);
        entity1.setLoginCount(5);
        entity1.setLastLogin(lastLogin);
        entity1.setTimestamp(timestamp);

        UserAnalytics entity2 = new UserAnalytics();
        entity2.setUserId(457L); // Разный userId
        entity2.setLoginCount(5);
        entity2.setLastLogin(lastLogin);
        entity2.setTimestamp(timestamp);

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    void shouldReturnCorrectToString() {
        UserAnalytics entity = new UserAnalytics();
        entity.setUserId(456L);
        entity.setLoginCount(5);
        entity.setLastLogin(LocalDateTime.parse("2023-12-01T10:15:30"));
        entity.setTimestamp(LocalDateTime.parse("2023-12-01T10:20:00"));

        String toString = entity.toString();

        assertThat(toString).isEqualTo(
                "UserAnalytics(userId=456," +
                        " loginCount=5," +
                        " lastLogin=2023-12-01T10:15:30," +
                        " timestamp=2023-12-01T10:20:00)");
    }

    @Test
    void shouldReturnCorrectToStringWithNullFields() {
        UserAnalytics entity = new UserAnalytics();
        entity.setUserId(456L);

        String toString = entity.toString();

        assertThat(toString).isEqualTo(
                "UserAnalytics(userId=456," +
                        " loginCount=0, lastLogin=null," +
                        " timestamp=null)");
    }
}
