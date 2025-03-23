package ru.pin36bik.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "presets")
public class Preset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VentilationFlag")
    private Boolean VentilationFlag;

    @Column(name = "VentilationTimer")
    private Integer VentilationTimer;

    @Column(name = "DarknessValue")
    private Integer DarknessValue;

    @Column(name = "FrameColor")
    private Integer FrameColor;
}