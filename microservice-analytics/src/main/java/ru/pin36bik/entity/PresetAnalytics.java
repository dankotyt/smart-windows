package ru.pin36bik.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "presets_analytics")
public class PresetAnalytics {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.
            ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "downloads_number", nullable = false)
    private Long downloadsNumber;

    @Column(name = "preset_name", nullable = false)
    private String presetName;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "PresetAnalytics("
                + "id=" + id
                + ", downloadsNumber=" + downloadsNumber
                + ", presetName=" + presetName
                + ", createdAt=" + (createdAt != null ? createdAt.
                format(FORMATTER) : null)
                + ')';
    }
}
