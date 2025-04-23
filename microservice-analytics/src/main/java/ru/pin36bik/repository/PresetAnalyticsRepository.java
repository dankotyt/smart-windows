package ru.pin36bik.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pin36bik.entity.PresetAnalytics;

@Repository
@Table(name = "presets_analytics")
public interface PresetAnalyticsRepository
        extends JpaRepository<PresetAnalytics, String> {
    @Query("SELECT p FROM PresetAnalytics p WHERE LOWER(p.presetName) = LOWER(:preset_name)")
    List<PresetAnalytics> findByPresetName(@Param("preset_name") String preset_name);

    @Query("SELECT p FROM PresetAnalytics p ORDER BY p.downloadsNumber DESC LIMIT 1")
    Optional<PresetAnalytics> findTopByDownloadsNumber();
}
