package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "Конфигурация пресета настроек окна")
public class PresetDTO {

    @Schema(description = "Уникальный идентификатор",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Название пресета",
            example = "Стандартные настройки")
    @JsonProperty("preset_name")
    @NotBlank(message = "Название пресета обязательно")
    private String presetName;

    @Schema(description = "Флаг проветривания",
            example = "true")
    @JsonProperty("ventilation_flag")
    @NotNull(message = "Флаг проветривания обязателен")
    private Boolean ventilationFlag;

    @Schema(description = "Время проветривания (секунды)",
            example = "600",
            minimum = "0",
            maximum = "86400")
    @JsonProperty("ventilation_timer")
    @NotNull(message = "Время проветривания обязательно")
    @Min(value = 0, message = "Минимальное время: 0 секунд")
    @Max(value = 86400, message = "Максимальное время: 86400 секунд (24 часа)")
    private Integer ventilationTimer;

    @Schema(description = "Уровень затемнения (0-100)",
            example = "75",
            minimum = "0",
            maximum = "100")
    @JsonProperty("darkness_value")
    @NotNull(message = "Уровень затемнения обязателен")
    @Min(value = 0, message = "Минимальное значение: 0")
    @Max(value = 100, message = "Максимальное значение: 100")
    private Integer darknessValue;

    @Schema(description = "Цвет подсветки (RGB)",
            example = "16777215")
    @JsonProperty("frame_color")
    @NotNull(message = "Цвет подсветки обязателен")
    private Integer frameColor;

    public PresetDTO() {
    }

    public PresetDTO(final Long myId, final String myPresetName,
                     final Boolean myVentilationFlag,
                     final Integer myVentilationTimer,
                     final Integer myDarknessValue,
                     final Integer myFrameColor) {
        this.id = myId;
        this.presetName = myPresetName;
        this.ventilationFlag = myVentilationFlag;
        this.ventilationTimer = myVentilationTimer;
        this.darknessValue = myDarknessValue;
        this.frameColor = myFrameColor;
    }
}
