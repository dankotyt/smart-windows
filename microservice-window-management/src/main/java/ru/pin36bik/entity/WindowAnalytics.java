package ru.pin36bik.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "window_analytics")
public class WindowAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "window_id", nullable = false)
    private Long windowId;

    @Column(name = "status", nullable = false)
    private boolean status;

}
