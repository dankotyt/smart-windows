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

@Setter
@Getter
@Entity
@Table(name = "users_analytics")
public class UserAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "login_count",
            nullable = false)
    private int loginCount;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "timestamp",
            nullable = false)
    private LocalDateTime timestamp;
}
