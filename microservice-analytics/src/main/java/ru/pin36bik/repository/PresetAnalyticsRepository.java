package ru.pin36bik.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pin36bik.entity.PresetAnalytics;

@Repository
public interface PresetAnalyticsRepository
        extends JpaRepository<PresetAnalytics, String> {

    @Query("SELECT p FROM PresetAnalytics p "
            + "WHERE LOWER(p.presetName) = LOWER(:presetName)")
    List<PresetAnalytics> findByPresetName(
            @Param("presetName") String presetName);

    @Query("SELECT p FROM PresetAnalytics p "
            + "ORDER BY p.downloadsNumber DESC LIMIT 1")
    Optional<PresetAnalytics> findTopByDownloadsNumber();

    @Modifying
    @Query("UPDATE PresetAnalytics p SET p.downloadsNumber = p.downloadsNumber + 1, "
            + "p.downloadsNumber = :now " //<- пофиксить
            + "WHERE p.presetName = :presetName")
    void incrementDownloadsNumber(@Param("presetName") String presetName,
                                  @Param("now") LocalDateTime now);
}
