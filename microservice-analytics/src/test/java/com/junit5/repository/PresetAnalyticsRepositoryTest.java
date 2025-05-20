package com.junit5.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import ru.pin36bik.AnalyticsApplication;
import ru.pin36bik.entity.PresetAnalytics;
import ru.pin36bik.repository.PresetAnalyticsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = AnalyticsApplication.class)
class PresetAnalyticsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PresetAnalyticsRepository repository;

    @Test
    void shouldFindByPresetNameCaseInsensitive() {
        PresetAnalytics preset1 = new PresetAnalytics();
        preset1.setPresetName("Morning Mode");
        preset1.setDownloadsNumber(100L);
        preset1.setCreatedAt(LocalDateTime.now());
        entityManager.persist(preset1);

        PresetAnalytics preset2 = new PresetAnalytics();
        preset2.setPresetName("Evening Mode");
        preset2.setDownloadsNumber(200L);
        preset2.setCreatedAt(LocalDateTime.now());
        entityManager.persist(preset2);

        entityManager.flush();

        List<PresetAnalytics> result = repository.findByPresetName("Morning Mode");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getPresetName()).isEqualTo("Morning Mode");
        assertThat(result.getFirst().getDownloadsNumber()).isEqualTo(100L);
    }

    @Test
    void shouldReturnEmptyListWhenPresetNameNotFound() {
        PresetAnalytics preset = new PresetAnalytics();
        preset.setPresetName("Morning Mode");
        preset.setDownloadsNumber(100L);
        preset.setCreatedAt(LocalDateTime.now());
        entityManager.persist(preset);
        entityManager.flush();

        List<PresetAnalytics> result = repository.findByPresetName("Night Mode");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindTopByDownloadsNumber() {
        PresetAnalytics preset1 = new PresetAnalytics();
        preset1.setPresetName("Morning Mode");
        preset1.setDownloadsNumber(100L);
        preset1.setCreatedAt(LocalDateTime.now());
        entityManager.persist(preset1);

        PresetAnalytics preset2 = new PresetAnalytics();
        preset2.setPresetName("Evening Mode");
        preset2.setDownloadsNumber(200L);
        preset2.setCreatedAt(LocalDateTime.now());
        entityManager.persist(preset2);

        entityManager.flush();

        Optional<PresetAnalytics> result = repository.findTopByDownloadsNumber();

        assertThat(result).isPresent();
        assertThat(result.get().getPresetName()).isEqualTo("Evening Mode");
        assertThat(result.get().getDownloadsNumber()).isEqualTo(200L);
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoPresetsExist() {
        Optional<PresetAnalytics> result = repository.findTopByDownloadsNumber();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldIncrementDownloadsNumber() {
        LocalDateTime now = LocalDateTime.now();
        PresetAnalytics preset = new PresetAnalytics();
        preset.setPresetName("Morning Mode");
        preset.setDownloadsNumber(100L);
        preset.setCreatedAt(now);
        entityManager.persist(preset);
        entityManager.flush();

        repository.incrementDownloadsNumber("Morning Mode", now);
        entityManager.flush();
        entityManager.clear();

        PresetAnalytics updatedPreset = entityManager.find(PresetAnalytics.class, preset.getId());
        assertThat(updatedPreset.getDownloadsNumber()).isEqualTo(101L);
    }

    @Test
    void shouldNotIncrementDownloadsNumberWhenPresetNotFound() {
        LocalDateTime now = LocalDateTime.now();
        PresetAnalytics preset = new PresetAnalytics();
        preset.setPresetName("Morning Mode");
        preset.setDownloadsNumber(100L);
        preset.setCreatedAt(now);
        entityManager.persist(preset);
        entityManager.flush();

        repository.incrementDownloadsNumber("Evening Mode", now);
        entityManager.flush();
        entityManager.clear();

        PresetAnalytics unchangedPreset = entityManager.find(PresetAnalytics.class, preset.getId());
        assertThat(unchangedPreset.getDownloadsNumber()).isEqualTo(100L);
    }

    @Test
    void shouldSaveAndFindById() {
        LocalDateTime now = LocalDateTime.now();
        PresetAnalytics preset = new PresetAnalytics();
        preset.setPresetName("Morning Mode");
        preset.setDownloadsNumber(100L);
        preset.setCreatedAt(now);
        PresetAnalytics savedPreset = repository.save(preset);

        Optional<PresetAnalytics> foundPreset = repository.findById(String.valueOf(savedPreset.getId()));

        assertThat(foundPreset).isPresent();
        assertThat(foundPreset.get().getPresetName()).isEqualTo("Morning Mode");
        assertThat(foundPreset.get().getDownloadsNumber()).isEqualTo(100L);
        assertThat(foundPreset.get().getCreatedAt()).isEqualTo(now);
    }

    @Test
    void shouldDeleteById() {
        LocalDateTime now = LocalDateTime.now();
        PresetAnalytics preset = new PresetAnalytics();
        preset.setPresetName("Morning Mode");
        preset.setDownloadsNumber(100L);
        preset.setCreatedAt(now);
        PresetAnalytics savedPreset = repository.save(preset);

        repository.deleteById(String.valueOf(savedPreset.getId()));
        Optional<PresetAnalytics> foundPreset = repository.findById(String.valueOf(savedPreset.getId()));

        assertThat(foundPreset).isEmpty();
    }
}
