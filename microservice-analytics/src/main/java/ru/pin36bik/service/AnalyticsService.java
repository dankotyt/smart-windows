package ru.pin36bik.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pin36bik.dto.PresetAnalyticsDTO;
import ru.pin36bik.dto.UserAnalyticsDTO;
import ru.pin36bik.entity.PresetAnalytics;
import ru.pin36bik.entity.UserAnalytics;
import ru.pin36bik.repository.PresetAnalyticsRepository;
import ru.pin36bik.repository.UserAnalyticsRepository;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserAnalyticsRepository userRepository;
    private final PresetAnalyticsRepository presetRepository;

    @Transactional
    public UserAnalyticsDTO saveUserAnalytics(
            final UserAnalyticsDTO dto) {
        UserAnalytics entity = new UserAnalytics();
        entity.setUserId(dto.getUserId());
        entity.setLoginCount(dto.getLoginCount());
        entity.setLastLogin(dto.getLastLogin());
        entity.setTimestamp(dto.getTimestamp()
                != null ? dto.getTimestamp() : LocalDateTime.now());

        UserAnalytics saved = userRepository.save(entity);
        return toUserDto(saved);
    }

    @Transactional
    public LocalDateTime recordUserLogin(
            final Long userId) {
        LocalDateTime now = LocalDateTime.now();
        userRepository.incrementLoginCount(userId, now);
        return now;
    }

    @Transactional(readOnly = true)
    public Optional<UserAnalyticsDTO> getUserAnalytics(
            final Long userId) {
        return userRepository.findById(userId).map(this::toUserDto);
    }

    @Transactional
    public PresetAnalyticsDTO savePresetAnalytics(
            final PresetAnalyticsDTO dto) {
        PresetAnalytics entity = new PresetAnalytics();
        entity.setDownloadsNumber((dto.getDownloadsNumber()));
        entity.setPresetName(dto.getPresetName());
        entity.setCreatedAt(dto.getCreatedAt()
                != null ? dto.getCreatedAt() : LocalDateTime.now());

        PresetAnalytics saved = presetRepository.save(entity);
        return toPresetDto(saved);
    }

    @Transactional(readOnly = true)
    public Optional<PresetAnalyticsDTO> getPresetAnalytics(
            final String presetName) {
        return presetRepository.findByPresetName(presetName)
                .stream()
                .findFirst()
                .map(this::toPresetDto);
    }

    @Transactional(readOnly = true)
    public Optional<PresetAnalyticsDTO> getMostDownloadedPreset() {
        return presetRepository.findTopByDownloadCount()
                .map(this::toPresetDto);
    }

    @Transactional(readOnly = true)
    public Optional<UserAnalyticsDTO> getUserWithEarliestLogin() {
        return userRepository.findOldestLoggedInUser()
                .map(this::toUserDto);
    }

    private UserAnalyticsDTO toUserDto(
            final UserAnalytics entity) {
        return new UserAnalyticsDTO(
                entity.getUserId(),
                entity.getLoginCount(),
                entity.getLastLogin(),
                entity.getTimestamp()
        );
    }

    private PresetAnalyticsDTO toPresetDto(
            final PresetAnalytics entity) {
        return new PresetAnalyticsDTO(
                entity.getId(),
                entity.getDownloadsNumber(),
                entity.getPresetName(),
                entity.getCreatedAt()
        );
    }
}
