package ru.pin36bik.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "window_id")
    private Long windowId;

    // Текущая погода
    private Double temperature;
    private String weatherText;
    private Boolean hasPrecipitation;
    private String precipitationType;
    private Double windSpeed;
    private Integer humidity; //влажность

    // Прогноз на ближайшее время
    private LocalDateTime forecastTime;
    private Double forecastTemperature;
    private String forecastCondition;

    private LocalDateTime lastUpdated;
}
