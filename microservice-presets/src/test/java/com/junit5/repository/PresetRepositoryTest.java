package com.junit5.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import ru.pin36bik.PresetsApplication;
import ru.pin36bik.entity.Preset;
import ru.pin36bik.repository.PresetRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = PresetsApplication.class)
class PresetRepositoryTest {

    @Autowired
    private PresetRepository presetRepository;

    @Test
    void testSaveAndFindById() {
        Preset preset = new Preset();
        preset.setPresetName("Test Preset");
        preset.setVentilationFlag(true);
        preset.setVentilationTimer(300);
        preset.setDarknessValue(75);
        preset.setFrameColor(0xFFFFFF);

        Preset savedPreset = presetRepository.save(preset);

        assertNotNull(savedPreset.getId());

        Optional<Preset> foundPreset = presetRepository.findById(savedPreset.getId());

        assertTrue(foundPreset.isPresent());
        assertEquals(savedPreset.getId(), foundPreset.get().getId());
        assertEquals("Test Preset", foundPreset.get().getPresetName());
        assertTrue(foundPreset.get().getVentilationFlag());
        assertEquals(300, foundPreset.get().getVentilationTimer());
        assertEquals(75, foundPreset.get().getDarknessValue());
        assertEquals(0xFFFFFF, foundPreset.get().getFrameColor());
    }

    @Test
    void testFindByPresetName() {
        Preset preset = new Preset();
        preset.setPresetName("Unique Preset");
        preset.setVentilationFlag(false);
        preset.setVentilationTimer(500);
        preset.setDarknessValue(50);
        preset.setFrameColor(0x000000);

        presetRepository.save(preset);

        Optional<Preset> foundPreset = presetRepository.findByPresetName("Unique Preset");

        assertTrue(foundPreset.isPresent());
        assertEquals("Unique Preset", foundPreset.get().getPresetName());
        assertFalse(foundPreset.get().getVentilationFlag());
        assertEquals(500, foundPreset.get().getVentilationTimer());
        assertEquals(50, foundPreset.get().getDarknessValue());
        assertEquals(0x000000, foundPreset.get().getFrameColor());
    }

    @Test
    void testFindByPresetNameNotFound() {
        Optional<Preset> foundPreset = presetRepository.findByPresetName("Nonexistent Preset");
        assertFalse(foundPreset.isPresent());
    }

    @Test
    void testUpdatePreset() {
        Preset preset = new Preset();
        preset.setPresetName("Original Preset");
        preset.setVentilationFlag(true);
        preset.setVentilationTimer(300);
        Preset savedPreset = presetRepository.save(preset);

        savedPreset.setPresetName("Updated Preset");
        savedPreset.setVentilationFlag(false);
        savedPreset.setVentilationTimer(600);
        presetRepository.save(savedPreset);

        Optional<Preset> updatedPreset = presetRepository.findById(savedPreset.getId());

        assertTrue(updatedPreset.isPresent());
        assertEquals("Updated Preset", updatedPreset.get().getPresetName());
        assertFalse(updatedPreset.get().getVentilationFlag());
        assertEquals(600, updatedPreset.get().getVentilationTimer());
    }

    @Test
    void testDeletePreset() {
        Preset preset = new Preset();
        preset.setPresetName("Preset to Delete");
        Preset savedPreset = presetRepository.save(preset);

        presetRepository.deleteById(savedPreset.getId());

        Optional<Preset> deletedPreset = presetRepository.findById(savedPreset.getId());
        assertFalse(deletedPreset.isPresent());
    }

    @Test
    void testSaveWithNullFields() {
        Preset preset = new Preset();
        preset.setPresetName(null); // Поле может быть null
        preset.setVentilationFlag(null);
        preset.setVentilationTimer(null);
        preset.setDarknessValue(null);
        preset.setFrameColor(null);

        Preset savedPreset = presetRepository.save(preset);

        Optional<Preset> foundPreset = presetRepository.findById(savedPreset.getId());
        assertTrue(foundPreset.isPresent());
        assertNull(foundPreset.get().getPresetName());
        assertNull(foundPreset.get().getVentilationFlag());
        assertNull(foundPreset.get().getVentilationTimer());
        assertNull(foundPreset.get().getDarknessValue());
        assertNull(foundPreset.get().getFrameColor());
    }
}