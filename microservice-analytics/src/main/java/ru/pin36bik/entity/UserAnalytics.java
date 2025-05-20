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

@Setter
@Getter
@Entity
@EqualsAndHashCode
@Table(name = "users_analytics")
public class UserAnalytics {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.
            ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "login_count",
            nullable = false)
    private int loginCount;

    @Column(name = "last_login")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLogin;

    @Column(name = "timestamp",
            nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "UserAnalytics("
                + "userId=" + userId
                + ", loginCount=" + loginCount
                + ", lastLogin=" + (lastLogin != null ? lastLogin
                .format(FORMATTER) : null)
                + ", timestamp=" + (timestamp != null ? timestamp
                .format(FORMATTER) : null)
                + ')';
    }
}
