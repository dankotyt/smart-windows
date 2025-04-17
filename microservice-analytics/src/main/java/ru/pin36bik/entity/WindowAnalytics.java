package ru.pin36bik.entity;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import java.time.LocalDateTime;

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