package ru.pin36bik.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pin36bik.entity.Preset;

@Repository
public interface PresetRepository extends JpaRepository<Preset, Long> {
    Optional<Preset> findByPresetName(String presetName);
}
