package ru.pin36bik.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user_analytics")
public class UserAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    @Column(name = "login_count",
            nullable = false)
    private int loginCount;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @ElementCollection
    @CollectionTable(name = "user_presets",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "preset_name")
    private List<String> presetNames;

    @Column(name = "timestamp",
            nullable = false)
    private LocalDateTime timestamp;
}
