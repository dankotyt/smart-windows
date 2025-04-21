package ru.pin36bik.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "presetName")
    private String presetName;

    @Column(name = "ventilationFlag")
    private Boolean ventilationFlag;

    @Column(name = "ventilationTimer")
    private Integer ventilationTimer;

    @Column(name = "darknessValue")
    private Integer darknessValue;

    @Column(name = "frameColor")
    private Integer frameColor;
}
