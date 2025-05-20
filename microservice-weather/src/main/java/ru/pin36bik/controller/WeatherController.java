package ru.pin36bik.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.dto.WeatherDTO;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.service.WeatherService;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/get_weather/{windowId}")
    public ResponseEntity<WeatherDTO> getWeather(
            @RequestHeader("X-Yandex-Weather-Key") String accessKey,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken,
            @PathVariable Long windowId) {

        log.info("Headers: X-Yandex-Weather-Key={}, X-User-Email={}, X-Valid-Token={}",
                accessKey, userEmail, validToken);
        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }
        return ResponseEntity.ok(weatherService.getWeather(accessKey, userEmail, windowId));
    }
}
