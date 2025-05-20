package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.pin36bik.dto.*;
import ru.pin36bik.entity.Weather;
import ru.pin36bik.entity.WeatherForecast;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.repository.WeatherRepository;
import ru.pin36bik.utils.WeatherMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final WeatherMapper weatherMapper;
    private final WeatherLocationProducer locationProducer;

    @Value("${YWAPI_KEY}")
    private String apiKey;

    public WeatherDTO getWeather(String accessKey, String userEmail, Long windowId) {
        CompletableFuture<WindowLocationDTO> locationFuture =
                locationProducer.requestLocation(windowId, userEmail);

        WindowLocationDTO location = locationFuture.join();

        if (!accessKey.equals(apiKey)) {
            throw new InvalidTokenException("Invalid access API key!");
        }
        return getWeatherByLocation(userEmail, windowId, location);
    }

    public WeatherDTO getWeatherByLocation(
            String userEmail,
            Long windowId,
            WindowLocationDTO locationDTO) {

        var weather = weatherRepository.findByWindowId(windowId)
                .orElseGet(() -> {
                    weatherRepository.deleteById(windowId);
                    var newWeather = new Weather();
                    newWeather.setWindowId(windowId);
                    newWeather.setUserEmail(userEmail);
                    newWeather.setLocation(locationDTO);
                    newWeather.setForecasts(new ArrayList<>());
                    return newWeather;
                });
        weather.setLocation(locationDTO);
        updateWeatherData(weather);
        return weatherMapper.toDto(weatherRepository.save(weather));
    }

    public void updateWeatherData(Weather weather) {
        try {
            YandexWeatherResponseDTO response = fetchWeatherData(
                    weather.getLocation().getLatitude(),
                    weather.getLocation().getLongitude()
            );

            if (response.getFact() != null) {
                updateCurrentWeather(weather, response.getFact());
            }

            if (response.getForecast() != null) {
                updateForecasts(weather, response.getForecast());
            }

            weather.setLastUpdated(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Failed to update weather for window {}: {}", weather.getWindowId(), e.getMessage());
        }
    }

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void scheduledWeatherUpdate() {
        weatherRepository.findAll().forEach(this::updateWeatherData);
    }

    private YandexWeatherResponseDTO fetchWeatherData(Double lat, Double lon) {
        String url = String.format("https://api.weather.yandex.ru/v2/forecast?lat=%s&lon=%s", lat, lon);
        return makeApiCall(url, YandexWeatherResponseDTO.class);
    }

    private <T> T makeApiCall(String url, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Yandex-Weather-Key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            log.info("Making request to Yandex Weather API: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    responseType
            );
            log.info("Received response from Yandex Weather API: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Yandex Weather API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error when calling Yandex Weather API", e);
            throw new RuntimeException("Failed to fetch weather data", e);
        }
    }

    private void updateCurrentWeather(Weather weather, YandexWeatherResponseDTO.FactDTO fact) {
        weather.setTemperature(fact.getTemp());
        weather.setHumidity(fact.getHumidity());
        weather.setPressure(fact.getPressure());
        weather.setPrecType(fact.getPrecType());
        weather.setPrecStrength(fact.getPrecStrength());
        weather.setWindDirection(fact.getWindDirection());
        weather.setWindSpeed(fact.getWindSpeed());
        weather.setCondition(fact.getCondition());
        weather.setCloudiness(fact.getCloudiness());
        weather.setDate(LocalDate.now());
    }

    public void updateForecasts(Weather weather, YandexWeatherResponseDTO.ForecastDTO forecast) {
        if (forecast == null || forecast.getParts() == null) return;

        if (weather.getForecasts() == null) {
            weather.setForecasts(new ArrayList<>());
        } else {
            weather.getForecasts().clear();
        }

        forecast.getParts().stream()
                .filter(part -> "today".equals(part.getPartName()) || "tomorrow".equals(part.getPartName()))
                .forEach(part -> {
                    WeatherForecast.ForecastType type = "today".equals(part.getPartName())
                            ? WeatherForecast.ForecastType.TODAY
                            : WeatherForecast.ForecastType.TOMORROW;

                    weather.getForecasts().add(
                            WeatherForecast.builder()
                                    .weather(weather)
                                    .type(type)
                                    .tempMin(part.getTempMin())
                                    .tempMax(part.getTempMax())
                                    .condition(part.getCondition())
                                    .date(LocalDate.now())
                                    .build()
                    );
                });
    }
}

