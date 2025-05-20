package ru.pin36bik.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastDTO {
    private ForecastType type; // Добавлен enum
    private LocalDate date;
    private Integer tempMin;
    private Integer tempMax;
    private String condition;

    public enum ForecastType {
        TODAY, TOMORROW
    }
}
