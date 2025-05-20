package ru.pin36bik.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.pin36bik.dto.WindowLocationDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "window_id")
    private Long windowId;

    @Column(name = "user_email")
    private String userEmail;

    @Embedded
    private WindowLocationDTO location;

    @Column(name = "temp")
    private Integer temperature;

    @Column(name = "humidity")
    private Integer humidity;

    @Column(name = "pressure")
    private Integer pressure;

    @Column(name = "prec_type")
    private String precType;

    @Column(name = "prec_strength")
    private String precStrength;

    @Column(name = "wind_direction")
    private String windDirection;

    @Column(name = "wind_speed")
    private Float windSpeed;

    @Column(name = "condition")
    private String condition;

    @Column(name = "cloudiness")
    private String cloudiness;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "weather", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeatherForecast> forecasts = new ArrayList<>();

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
