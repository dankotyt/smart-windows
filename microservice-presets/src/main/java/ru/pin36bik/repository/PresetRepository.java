package ru.pin36bik.repository;

import ru.pin36bik.entity.Preset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PresetRepository extends JpaRepository<Preset, Long> {
    Optional<Preset> findByPresetName(String presetName);
}