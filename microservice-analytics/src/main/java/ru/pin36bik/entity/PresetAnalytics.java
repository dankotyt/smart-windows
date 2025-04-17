package ru.pin36bik.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "presets")
public class PresetAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "presetName", nullable = false)
    private String presetName;

    @Column(name = "downloaded_at", nullable = false)
    private LocalDateTime downloadedAt;
}