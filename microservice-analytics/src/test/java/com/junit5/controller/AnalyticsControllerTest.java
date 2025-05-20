package com.junit5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.pin36bik.controller.AnalyticsController;
import ru.pin36bik.dto.PresetAnalyticsDTO;
import ru.pin36bik.dto.UserAnalyticsDTO;
import ru.pin36bik.service.AnalyticsService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private AnalyticsController analyticsController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(analyticsController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void createPreset_ShouldReturnBadRequest_WhenNameIsBlank() throws Exception {
        PresetAnalyticsDTO invalidPreset = new PresetAnalyticsDTO(
                1L,
                0L,
                "",
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/v0/analytics/presets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPreset)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void createPreset_ShouldReturnCreatedPreset() throws Exception {
        PresetAnalyticsDTO preset = new PresetAnalyticsDTO(
                1L,
                0L,
                "Morning Mode",
                LocalDateTime.parse("2023-12-01T10:15:30")
        );

        when(analyticsService.savePresetAnalytics(any(PresetAnalyticsDTO.class)))
                .thenReturn(preset);

        mockMvc.perform(post("/api/v0/analytics/presets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(preset)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.preset_name").value("Morning Mode"))
                .andExpect(jsonPath("$.downloads_number").value(0))
                .andExpect(jsonPath("$.created_at").value("2023-12-01T10:15:30"));
    }

    @Test
    void getPresetByName_ShouldReturnPreset() throws Exception {
        PresetAnalyticsDTO preset = new PresetAnalyticsDTO(
                2L,
                150L,
                "Evening Mode",
                LocalDateTime.parse("2023-12-02T18:30:00")
        );

        when(analyticsService.getPresetAnalytics("Evening Mode"))
                .thenReturn(Optional.of(preset));

        mockMvc.perform(get("/api/v0/analytics/presets/get-preset-by-name/Evening Mode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.preset_name").value("Evening Mode"))
                .andExpect(jsonPath("$.downloads_number").value(150))
                .andExpect(jsonPath("$.created_at").value("2023-12-02T18:30:00"));
    }

    @Test
    void getPresetByName_ShouldReturnEmptyWhenNotFound() throws Exception {
        when(analyticsService.getPresetAnalytics("Unknown"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v0/analytics/presets/get-preset-by-name/Unknown"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void recordPresetDownload_ShouldReturnTimestamp() throws Exception {
        LocalDateTime timestamp = LocalDateTime.parse("2023-12-03T14:25:00");

        when(analyticsService.recordPresetDownload(anyString()))
                .thenReturn(timestamp);

        mockMvc.perform(post("/api/v0/analytics/presets/downloads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Popular Preset\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("2023-12-03T14:25:00"));
    }

    @Test
    void getMostDownloadedPreset_ShouldReturnPreset() throws Exception {
        PresetAnalyticsDTO popularPreset = new PresetAnalyticsDTO(
                3L,
                1000L,
                "Popular Preset",
                LocalDateTime.parse("2023-11-15T09:00:00")
        );

        when(analyticsService.getMostDownloadedPreset())
                .thenReturn(Optional.of(popularPreset));

        mockMvc.perform(get("/api/v0/analytics/presets/most-downloaded"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.preset_name").value("Popular Preset"))
                .andExpect(jsonPath("$.downloads_number").value(1000));
    }

    @Test
    void getMostDownloadedPreset_ShouldReturnNotFound() throws Exception {
        Mockito.when(analyticsService.getMostDownloadedPreset())
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v0/analytics/presets/most-downloaded"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        UserAnalyticsDTO user = new UserAnalyticsDTO();
        user.setUserId(1L);
        user.setLoginCount(0);

        when(analyticsService.saveUserAnalytics(any(UserAnalyticsDTO.class)))
                .thenReturn(user);

        mockMvc.perform(post("/api/v0/analytics/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1L))
                .andExpect(jsonPath("$.login_count").value(0));
    }

    @Test
    void recordUserLogin_ShouldReturnTimestamp() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Mockito.when(analyticsService.recordUserLogin(anyLong()))
                .thenReturn(now);

        mockMvc.perform(post("/api/v0/analytics/users/logins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("12345"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserAnalyticsDTO user = new UserAnalyticsDTO();
        user.setUserId(1L);

        Mockito.when(analyticsService.getUserAnalytics(anyLong()))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v0/analytics/users/get-user-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1));
    }

    @Test
    void getUserById_ShouldReturnNotFound() throws Exception {
        Mockito.when(analyticsService.getUserAnalytics(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v0/analytics/users/get-user-by-id/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void getUserWithEarliestLogin_ShouldReturnUser() throws Exception {
        UserAnalyticsDTO user = new UserAnalyticsDTO();
        user.setUserId(1L);

        Mockito.when(analyticsService.getUserWithEarliestLogin())
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v0/analytics/users/earliest-login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1));
    }

    @Test
    void getUserWithEarliestLogin_ShouldReturnNotFound() throws Exception {
        Mockito.when(analyticsService.getUserWithEarliestLogin())
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v0/analytics/users/earliest-login"))
                .andExpect(status().isNotFound());
    }
}
