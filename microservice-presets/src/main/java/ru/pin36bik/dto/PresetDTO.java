package ru.pin36bik.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "DTO, отображающий конфигурацию пресета настроек окна")
public class PresetDTO {
    @Schema(
            description = "Уникальный идентификатор пресета",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Название пресета",
            example = "Стандартные настройки"
    )
    @NotNull(message = "Название пресета не может быть null-значением!")
    private String presetName;

    @Schema(
            description = "Индикатор, показывающий, включено ли проветривание",
            example = "true"
    )
    @NotNull(message = "Индикатор включения проветривания не может быть null-значением!")
    private Boolean ventilationFlag;

    @Schema(
            description = "Время проветривания в секундах",
            example = "600",
            minimum = "0",
            maximum = "86400" // 24 часа
    )
    @NotNull(message = "Время проветривания не может быть null-значением!")
    @Min(value = 0, message = "Время проветривания не может быть меньше 0!")
    @Max(value = 1440, message = "Время проветривания не может превышать 86400 секунд! (24 часа)")
    private Integer ventilationTimer;

    @Schema(
            description = "Уровень затемнения окна (0-100)",
            example = "75",
            minimum = "0",
            maximum = "100"
    )
    @NotNull(message = "Уровень затемнения не может быть null-значением!")
    @Min(value = 0, message = "Уровень затемнения не может быть меньше 0!")
    @Max(value = 100, message = "Уровень затемнения не может быть больше 100!")
    private Integer darknessValue;

    @Schema(
            description = "Цвет подсветки рамы в формате RGB (в десятичной системе)",
            example = "16777215" // Белый
    )
    @NotNull(message = "Цвет подсветки рамы не может быть null-значением!")
    private Integer frameColor;

    public PresetDTO() {
    }

    public PresetDTO(Long id, String presetName, Boolean ventilationFlag,
                     Integer ventilationTimer, Integer darknessValue, Integer frameColor) {
        this.id = id;
        this.presetName = presetName;
        this.ventilationFlag = ventilationFlag;
        this.ventilationTimer = ventilationTimer;
        this.darknessValue = darknessValue;
        this.frameColor = frameColor;
    }
}