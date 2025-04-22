package ru.pin36bik.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pin36bik.entity.PresetAnalytics;

@Repository
@Table(name = "presets_analytics")
public interface PresetAnalyticsRepository
        extends JpaRepository<PresetAnalytics, String> {
    List<PresetAnalytics> findByPresetName(String name);

    @Query("SELECT p FROM Preset p ORDER BY p.downloadCount DESC LIMIT 1")
    Optional<PresetAnalytics> findTopByDownloadCount();
}
