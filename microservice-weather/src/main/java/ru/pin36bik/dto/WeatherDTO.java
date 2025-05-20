package ru.pin36bik.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO {
    private Long windowId;
    private String userEmail;
    private WindowLocationDTO location;

    private WeatherCurrentDTO current;

    private List<WeatherForecastDTO> forecasts;

    private LocalDateTime lastUpdated;
}
