package ru.pin36bik.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PresetDTO {
    private Long id;

    @NotNull
    private Boolean VentilationFlag;

    @NotNull
    private Integer VentilationTimer;

    @NotNull
    private Integer DarknessValue;

    @NotNull
    private Integer FrameColor;
}