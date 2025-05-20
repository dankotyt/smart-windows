package ru.pin36bik.utils;

import org.springframework.stereotype.Component;
import ru.pin36bik.dto.WeatherCurrentDTO;
import ru.pin36bik.dto.WeatherDTO;
import ru.pin36bik.dto.WeatherForecastDTO;
import ru.pin36bik.entity.Weather;
import ru.pin36bik.entity.WeatherForecast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherMapper {
    public WeatherDTO toDto(Weather weather) {
        return WeatherDTO.builder()
                .windowId(weather.getWindowId())
                .userEmail(weather.getUserEmail())
                .location(weather.getLocation())
                .current(toCurrentDto(weather))
                .forecasts(toForecastDto(weather.getForecasts()))
                .lastUpdated(weather.getLastUpdated())
                .build();
    }

    private WeatherCurrentDTO toCurrentDto(Weather weather) {
        if (weather == null) return null;
        return WeatherCurrentDTO.builder()
                .temperature(weather.getTemperature())
                .humidity(weather.getHumidity())
                .pressure(weather.getPressure())
                .precType(weather.getPrecType())
                .precStrength(weather.getPrecStrength())
                .windDirection(weather.getWindDirection())
                .windSpeed(weather.getWindSpeed())
                .condition(weather.getCondition())
                .cloudiness(weather.getCloudiness())
                .build();
    }

    private List<WeatherForecastDTO> toForecastDto(List<WeatherForecast> forecasts) {
        if (forecasts == null || forecasts.isEmpty()) return Collections.emptyList();

        return forecasts.stream()
                .map(this::toForecastDto)
                .collect(Collectors.toList());
    }

    private WeatherForecastDTO.ForecastType mapForecastType(WeatherForecast.ForecastType entityType) {
        if (entityType == null) return null;
        return WeatherForecastDTO.ForecastType.valueOf(entityType.name());
    }

    private WeatherForecastDTO toForecastDto(WeatherForecast forecast) {
        if (forecast == null) return null;

        return WeatherForecastDTO.builder()
                .type(mapForecastType(forecast.getType()))
                .tempMin(forecast.getTempMin())
                .tempMax(forecast.getTempMax())
                .condition(forecast.getCondition())
                .date(forecast.getDate())
                .build();
    }
}
