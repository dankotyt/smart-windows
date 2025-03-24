package ru.pin36bik.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class PresetDTO {
    private Long id;

    @NotNull
    private String PresetName;

    @NotNull
    private Boolean VentilationFlag;

    @NotNull
    private Integer VentilationTimer;

    @NotNull
    private Integer DarknessValue;

    @NotNull
    private Integer FrameColor;

    public PresetDTO(Long presetID, String presetName, Boolean ventilationFlag, Integer ventilationTimer, Integer darknessValue, Integer frameColor) {
        this.id = presetID;
        this.PresetName = presetName;
        this.VentilationFlag = ventilationFlag;
        this.VentilationTimer = ventilationTimer;
        this.DarknessValue = darknessValue;
        this.FrameColor = frameColor;
    }
}