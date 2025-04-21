package ru.pin36bik.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pin36bik.dto.PresetAnalyticsDTO;
import ru.pin36bik.dto.UserAnalyticsDTO;
import ru.pin36bik.dto.WindowAnalyticsDTO;
import ru.pin36bik.entity.PresetAnalytics;
import ru.pin36bik.entity.UserAnalytics;
import ru.pin36bik.entity.WindowAnalytics;
import ru.pin36bik.repository.PresetAnalyticsRepository;
import ru.pin36bik.repository.UserAnalyticsRepository;
import ru.pin36bik.repository.WindowAnalyticsRepository;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final WindowAnalyticsRepository windowRepository;
    private final UserAnalyticsRepository userRepository;
    private final PresetAnalyticsRepository presetRepository;

    @Transactional
    public WindowAnalyticsDTO saveWindowAnalytics(
            final WindowAnalyticsDTO dto) {
        WindowAnalytics entity = new WindowAnalytics();
        entity.setWindowId(dto.getWindowId());
        entity.setActive(dto.isActive());
        entity.setSectionsCount(dto.getSectionsCount());
        entity.setHeight(dto.getHeight());
        entity.setSectionSize(dto.getSectionSize());
        entity.setTimestamp(dto.getTimestamp()
                != null ? dto.getTimestamp() : LocalDateTime.now());

        WindowAnalytics saved = windowRepository.save(entity);
        return toWindowDto(saved);
    }

    @Transactional(readOnly = true)
    public Optional<WindowAnalyticsDTO> getWindowAnalytics(
            final String windowId) {
        return windowRepository.findById(windowId).map(this::toWindowDto);
    }

    @Transactional(readOnly = true)
    public List<WindowAnalyticsDTO> getWindowsByStatus(
            final boolean isActive) {
        return windowRepository.findByActive(isActive).stream()
                .map(this::toWindowDto)
                .toList();
    }

    @Transactional
    public UserAnalyticsDTO saveUserAnalytics(
            final UserAnalyticsDTO dto) {
        UserAnalytics entity = new UserAnalytics();
        entity.setUserId(dto.getUserId());
        entity.setLoginCount(dto.getLoginCount());
        entity.setLastLogin(dto.getLastLogin());
        entity.setPresetNames(dto.getPresetNames());
        entity.setTimestamp(dto.getTimestamp()
                != null ? dto.getTimestamp() : LocalDateTime.now());

        UserAnalytics saved = userRepository.save(entity);
        return toUserDto(saved);
    }

    @Transactional
    public LocalDateTime recordUserLogin(
            final String userId) {
        LocalDateTime now = LocalDateTime.now();
        userRepository.incrementLoginCount(userId, now);
        return now;
    }

    @Transactional(readOnly = true)
    public Optional<UserAnalyticsDTO> getUserAnalytics(
            final String userId) {
        return userRepository.findById(userId).map(this::toUserDto);
    }

    @Transactional
    public PresetAnalyticsDTO savePresetAnalytics(
            final PresetAnalyticsDTO dto) {
        PresetAnalytics entity = new PresetAnalytics();
        entity.setUserId(dto.getUserId());
        entity.setPresetName(dto.getPresetName());
        entity.setDownloadedAt(dto.getDownloadedAt()
                != null ? dto.getDownloadedAt() : LocalDateTime.now());

        PresetAnalytics saved = presetRepository.save(entity);
        return toPresetDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PresetAnalyticsDTO> getUserPresets(
            final String userId) {
        return presetRepository.findByUserId(userId).stream()
                .map(this::toPresetDto)
                .toList();
    }

    private WindowAnalyticsDTO toWindowDto(
            final WindowAnalytics entity) {
        return new WindowAnalyticsDTO(
                entity.getWindowId(),
                entity.isActive(),
                entity.getSectionsCount(),
                entity.getHeight(),
                entity.getSectionSize(),
                entity.getTimestamp()
        );
    }

    private UserAnalyticsDTO toUserDto(
            final UserAnalytics entity) {
        return new UserAnalyticsDTO(
                entity.getUserId(),
                entity.getLoginCount(),
                entity.getLastLogin(),
                entity.getPresetNames(),
                entity.getTimestamp()
        );
    }

    private PresetAnalyticsDTO toPresetDto(
            final PresetAnalytics entity) {
        return new PresetAnalyticsDTO(
                entity.getId(),
                entity.getUserId(),
                entity.getPresetName(),
                entity.getDownloadedAt()
        );
    }
}
