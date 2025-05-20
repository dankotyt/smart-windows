package ru.pin36bik.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WeatherCurrentDTO {
    private Integer temperature;
    private Integer humidity;
    private Integer pressure;
    private String precType;
    private String precStrength;
    private String windDirection;
    private Float windSpeed;
    private String condition;
    private String cloudiness;
}
