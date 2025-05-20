package com.junit5.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.pin36bik.dto.PresetAnalyticsDTO;
import ru.pin36bik.dto.UserAnalyticsDTO;
import ru.pin36bik.entity.PresetAnalytics;
import ru.pin36bik.entity.UserAnalytics;
import ru.pin36bik.repository.PresetAnalyticsRepository;
import ru.pin36bik.repository.UserAnalyticsRepository;
import ru.pin36bik.service.AnalyticsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @InjectMocks
    private AnalyticsService analyticsService;

    @Mock
    private UserAnalyticsRepository userRepository;

    @Mock
    private PresetAnalyticsRepository presetRepository;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
    }

    @Test
    void shouldSaveUserAnalyticsWithProvidedTimestamp() {
        UserAnalyticsDTO dto = new UserAnalyticsDTO(1L, 5, now.minusDays(1), now);
        UserAnalytics savedEntity = new UserAnalytics();
        savedEntity.setUserId(1L);
        savedEntity.setLoginCount(5);
        savedEntity.setLastLogin(now.minusDays(1));
        savedEntity.setTimestamp(now);

        when(userRepository.save(any(UserAnalytics.class))).thenReturn(savedEntity);

        UserAnalyticsDTO result = analyticsService.saveUserAnalytics(dto);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getLoginCount()).isEqualTo(5);
        assertThat(result.getLastLogin()).isEqualTo(now.minusDays(1));
        assertThat(result.getTimestamp()).isEqualTo(now);

        verify(userRepository, times(1)).save(any(UserAnalytics.class));
    }

    @Test
    void shouldSaveUserAnalyticsWithCurrentTimestampWhenNull() {
        UserAnalyticsDTO dto = new UserAnalyticsDTO(1L, 5, now.minusDays(1), null);
        UserAnalytics savedEntity = new UserAnalytics();
        savedEntity.setUserId(1L);
        savedEntity.setLoginCount(5);
        savedEntity.setLastLogin(now.minusDays(1));
        savedEntity.setTimestamp(now);

        when(userRepository.save(any(UserAnalytics.class))).thenReturn(savedEntity);

        UserAnalyticsDTO result = analyticsService.saveUserAnalytics(dto);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getLoginCount()).isEqualTo(5);
        assertThat(result.getLastLogin()).isEqualTo(now.minusDays(1));
        assertThat(result.getTimestamp()).isNotNull();

        verify(userRepository, times(1)).save(any(UserAnalytics.class));
    }

    @Test
    void shouldRecordUserLogin() {
        Long userId = 1L;

        LocalDateTime result = analyticsService.recordUserLogin(userId);

        assertThat(result).isNotNull();
        verify(userRepository, times(1)).incrementLoginCount(userId, result);
    }

    @Test
    void shouldGetUserAnalyticsWhenUserExists() {
        Long userId = 1L;
        UserAnalytics entity = new UserAnalytics();
        entity.setUserId(userId);
        entity.setLoginCount(5);
        entity.setLastLogin(now.minusDays(1));
        entity.setTimestamp(now);

        when(userRepository.findByUserId(userId)).thenReturn(List.of(entity));

        Optional<UserAnalyticsDTO> result = analyticsService.getUserAnalytics(userId);

        assertThat(result).isPresent();
        UserAnalyticsDTO dto = result.get();
        assertThat(dto.getUserId()).isEqualTo(userId);
        assertThat(dto.getLoginCount()).isEqualTo(5);
        assertThat(dto.getLastLogin()).isEqualTo(now.minusDays(1));
        assertThat(dto.getTimestamp()).isEqualTo(now);

        verify(userRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findByUserId(userId)).thenReturn(List.of());

        Optional<UserAnalyticsDTO> result = analyticsService.getUserAnalytics(userId);

        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserIdIsNull() {
        Optional<UserAnalyticsDTO> result = analyticsService.getUserAnalytics(null);

        assertThat(result).isEmpty();
        verify(userRepository, never()).findByUserId(any());
    }

    @Test
    void shouldSavePresetAnalyticsWithProvidedCreatedAt() {
        PresetAnalyticsDTO dto = new PresetAnalyticsDTO(1L, 100L, "Morning Mode", now);
        PresetAnalytics savedEntity = new PresetAnalytics();
        savedEntity.setId(1L);
        savedEntity.setDownloadsNumber(100L);
        savedEntity.setPresetName("Morning Mode");
        savedEntity.setCreatedAt(now);

        when(presetRepository.save(any(PresetAnalytics.class))).thenReturn(savedEntity);

        PresetAnalyticsDTO result = analyticsService.savePresetAnalytics(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDownloadsNumber()).isEqualTo(100L);
        assertThat(result.getPresetName()).isEqualTo("Morning Mode");
        assertThat(result.getCreatedAt()).isEqualTo(now);

        verify(presetRepository, times(1)).save(any(PresetAnalytics.class));
    }

    @Test
    void shouldSavePresetAnalyticsWithCurrentCreatedAtWhenNull() {
        PresetAnalyticsDTO dto = new PresetAnalyticsDTO(1L, 100L, "Morning Mode", null);
        PresetAnalytics savedEntity = new PresetAnalytics();
        savedEntity.setId(1L);
        savedEntity.setDownloadsNumber(100L);
        savedEntity.setPresetName("Morning Mode");
        savedEntity.setCreatedAt(now);

        when(presetRepository.save(any(PresetAnalytics.class))).thenReturn(savedEntity);

        PresetAnalyticsDTO result = analyticsService.savePresetAnalytics(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDownloadsNumber()).isEqualTo(100L);
        assertThat(result.getPresetName()).isEqualTo("Morning Mode");
        assertThat(result.getCreatedAt()).isNotNull();

        verify(presetRepository, times(1)).save(any(PresetAnalytics.class));
    }

    @Test
    void shouldRecordPresetDownload() {
        String presetName = "Morning Mode";

        LocalDateTime result = analyticsService.recordPresetDownload(presetName);

        assertThat(result).isNotNull();
        verify(presetRepository, times(1)).incrementDownloadsNumber(presetName, now);
    }

    @Test
    void shouldGetPresetAnalyticsWhenPresetExists() {
        String presetName = "Morning Mode";
        PresetAnalytics entity = new PresetAnalytics();
        entity.setId(1L);
        entity.setDownloadsNumber(100L);
        entity.setPresetName(presetName);
        entity.setCreatedAt(now);

        when(presetRepository.findByPresetName(presetName)).thenReturn(List.of(entity));

        Optional<PresetAnalyticsDTO> result = analyticsService.getPresetAnalytics(presetName);

        assertThat(result).isPresent();
        PresetAnalyticsDTO dto = result.get();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDownloadsNumber()).isEqualTo(100L);
        assertThat(dto.getPresetName()).isEqualTo(presetName);
        assertThat(dto.getCreatedAt()).isEqualTo(now);

        verify(presetRepository, times(1)).findByPresetName(presetName);
    }

    @Test
    void shouldReturnEmptyOptionalWhenPresetNotFound() {
        String presetName = "Morning Mode";
        when(presetRepository.findByPresetName(presetName)).thenReturn(List.of());

        Optional<PresetAnalyticsDTO> result = analyticsService.getPresetAnalytics(presetName);

        assertThat(result).isEmpty();
        verify(presetRepository, times(1)).findByPresetName(presetName);
    }
    @Test
    void shouldReturnEmptyOptionalWhenPresetNameIsBlank() {
        Optional<PresetAnalyticsDTO> result = analyticsService.getPresetAnalytics("   ");

        assertThat(result).isEmpty();
        verify(presetRepository, never()).findByPresetName(any());
    }

    @Test
    void shouldGetMostDownloadedPresetWhenExists() {
        PresetAnalytics entity = new PresetAnalytics();
        entity.setId(1L);
        entity.setDownloadsNumber(100L);
        entity.setPresetName("Morning Mode");
        entity.setCreatedAt(now);

        when(presetRepository.findTopByDownloadsNumber()).thenReturn(Optional.of(entity));

        Optional<PresetAnalyticsDTO> result = analyticsService.getMostDownloadedPreset();

        assertThat(result).isPresent();
        PresetAnalyticsDTO dto = result.get();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDownloadsNumber()).isEqualTo(100L);
        assertThat(dto.getPresetName()).isEqualTo("Morning Mode");
        assertThat(dto.getCreatedAt()).isEqualTo(now);

        verify(presetRepository, times(1)).findTopByDownloadsNumber();
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoMostDownloadedPreset() {
        when(presetRepository.findTopByDownloadsNumber()).thenReturn(Optional.empty());

        Optional<PresetAnalyticsDTO> result = analyticsService.getMostDownloadedPreset();

        assertThat(result).isEmpty();
        verify(presetRepository, times(1)).findTopByDownloadsNumber();
    }

    @Test
    void shouldGetUserWithEarliestLoginWhenExists() {
        UserAnalytics entity = new UserAnalytics();
        entity.setUserId(1L);
        entity.setLoginCount(5);
        entity.setLastLogin(now.minusDays(1));
        entity.setTimestamp(now);

        when(userRepository.findOldestLoggedInUser()).thenReturn(Optional.of(entity));

        Optional<UserAnalyticsDTO> result = analyticsService.getUserWithEarliestLogin();

        assertThat(result).isPresent();
        UserAnalyticsDTO dto = result.get();
        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getLoginCount()).isEqualTo(5);
        assertThat(dto.getLastLogin()).isEqualTo(now.minusDays(1));
        assertThat(dto.getTimestamp()).isEqualTo(now);

        verify(userRepository, times(1)).findOldestLoggedInUser();
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoUserWithEarliestLogin() {
        when(userRepository.findOldestLoggedInUser()).thenReturn(Optional.empty());

        Optional<UserAnalyticsDTO> result = analyticsService.getUserWithEarliestLogin();

        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findOldestLoggedInUser();
    }
}