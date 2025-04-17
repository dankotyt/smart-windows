package ru.pin36bik.entity;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "user_analytics")
public class UserAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    @Column(name = "login_count", nullable = false)
    private int loginCount;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @ElementCollection
    @CollectionTable(name = "user_presets", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "preset_name")
    private List<String> presetNames;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
