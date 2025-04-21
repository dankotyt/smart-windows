package ru.pin36bik.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin36bik.entity.PresetAnalytics;

public interface PresetAnalyticsRepository
        extends JpaRepository<PresetAnalytics, Long> {
    List<PresetAnalytics> findByUserId(String userId);

    List<PresetAnalytics> findByPresetName(String name);
}
