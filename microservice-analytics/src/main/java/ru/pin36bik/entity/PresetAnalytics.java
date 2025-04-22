package ru.pin36bik.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "presets_analytics")
public class PresetAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "downloads_number", nullable = false)
    private Long downloadsNumber;

    @Column(name = "presetName", nullable = false)
    private String presetName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
