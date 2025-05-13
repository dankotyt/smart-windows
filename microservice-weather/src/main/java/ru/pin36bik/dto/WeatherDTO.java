package ru.pin36bik.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WeatherDTO {
    private Long windowId;
    private Double temperature;
    private String weatherText;
    private Boolean hasPrecipitation;
    private String precipitationType;
    private Double windSpeed;
    private Integer humidity;
    private LocalDateTime forecastTime;
    private Double forecastTemperature;
    private String forecastCondition;
    private LocalDateTime lastUpdated;
}
