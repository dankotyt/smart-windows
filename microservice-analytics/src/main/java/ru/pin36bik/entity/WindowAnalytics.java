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

@Entity
@Setter
@Getter
@Table(name = "window_analytics")
public class WindowAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String windowId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "sections_count", nullable = false)
    private int sectionsCount;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "section_size", nullable = false)
    private double sectionSize;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
