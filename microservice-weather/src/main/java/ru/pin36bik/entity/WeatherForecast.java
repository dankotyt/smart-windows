package ru.pin36bik.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "weather_forecasts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "weather_id")
    private Weather weather;

    @Enumerated(EnumType.STRING)
    @Column(name = "forecast_type")
    private ForecastType type;

    @Column(name = "temp_min")
    private Integer tempMin;

    @Column(name = "temp_max")
    private Integer tempMax;

    @Column(name = "condition")
    private String condition;

    private LocalDate date;

    public enum ForecastType {
        TODAY, TOMORROW
    }
}
