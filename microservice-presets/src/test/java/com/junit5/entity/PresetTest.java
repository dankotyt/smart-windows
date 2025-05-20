package com.junit5.entity;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import ru.pin36bik.entity.Preset;

import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class PresetTest {

    @Test
    void testEntityAnnotations() {
        // Проверка аннотаций класса
        assertTrue(Preset.class.isAnnotationPresent(Entity.class));
        assertTrue(Preset.class.isAnnotationPresent(jakarta.persistence.Table.class));
        assertEquals("presets", Preset.class.getAnnotation(jakarta.persistence.Table.class).name());
    }

    @Test
    void testIdField() throws NoSuchFieldException {
        Field idField = Preset.class.getDeclaredField("id");

        assertTrue(idField.isAnnotationPresent(Id.class));
        assertTrue(idField.isAnnotationPresent(GeneratedValue.class));
        assertEquals(GenerationType.IDENTITY,
                idField.getAnnotation(GeneratedValue.class).strategy());
    }

    @Test
    void testColumnMappings() throws NoSuchFieldException {
        checkColumnAnnotation("presetName", "presetName");
        checkColumnAnnotation("ventilationFlag", "ventilationFlag");
        checkColumnAnnotation("ventilationTimer", "ventilationTimer");
        checkColumnAnnotation("darknessValue", "darknessValue");
        checkColumnAnnotation("frameColor", "frameColor");
    }

    private void checkColumnAnnotation(String fieldName, String expectedColumnName)
            throws NoSuchFieldException {
        Field field = Preset.class.getDeclaredField(fieldName);
        assertTrue(field.isAnnotationPresent(Column.class));
        assertEquals(expectedColumnName,
                field.getAnnotation(Column.class).name());
    }

    @Test
    void testLombokFunctionality() {
        Preset preset = new Preset();
        preset.setId(1L);
        preset.setPresetName("Test Preset");
        preset.setVentilationFlag(true);
        preset.setVentilationTimer(300);
        preset.setDarknessValue(75);
        preset.setFrameColor(0xFFFFFF);

        assertAll(
                () -> assertEquals(1L, preset.getId()),
                () -> assertEquals("Test Preset", preset.getPresetName()),
                () -> assertTrue(preset.getVentilationFlag()),
                () -> assertEquals(300, preset.getVentilationTimer()),
                () -> assertEquals(75, preset.getDarknessValue()),
                () -> assertEquals(0xFFFFFF, preset.getFrameColor())
        );
    }

    @Test
    void testNoArgsConstructor() {
        Preset preset = new Preset();
        assertNotNull(preset);
    }

    @Test
    void testFieldNullability() {
        Preset preset = new Preset();
        preset.setId(null);
        preset.setPresetName(null);
        preset.setVentilationFlag(null);
        preset.setVentilationTimer(null);
        preset.setDarknessValue(null);
        preset.setFrameColor(null);

        assertAll(
                () -> assertNull(preset.getId()),
                () -> assertNull(preset.getPresetName()),
                () -> assertNull(preset.getVentilationFlag()),
                () -> assertNull(preset.getVentilationTimer()),
                () -> assertNull(preset.getDarknessValue()),
                () -> assertNull(preset.getFrameColor())
        );
    }
}