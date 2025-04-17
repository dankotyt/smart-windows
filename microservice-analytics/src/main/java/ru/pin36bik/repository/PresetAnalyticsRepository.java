package ru.pin36bik.repository;

import ru.pin36bik.entity.PresetAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PresetAnalyticsRepository extends JpaRepository<PresetAnalytics, Long> {
    List<PresetAnalytics> findByUserId(String userId);
    List<PresetAnalytics> findByPresetName(String name);
}
