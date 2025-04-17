package ru.pin36bik.controller;

import ru.pin36bik.dto.*;
import ru.pin36bik.service.AnalyticsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/analytics",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Window Analytics API", description = "API для работы с аналитикой умных окон")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping(value = "/windows", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Сохранить данные окна")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные сохранены",
                    content = @Content(schema = @Schema(implementation = WindowAnalyticsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные данные")
    })
    public ResponseEntity<WindowAnalyticsDTO> saveWindowAnalytics(
            @Parameter(description = "Данные окна в формате JSON", required = true)
            @Valid @RequestBody WindowAnalyticsDTO dto) {
        return ResponseEntity.ok(analyticsService.saveWindowAnalytics(dto));
    }

    @GetMapping("/windows/{windowId}")
    @Operation(summary = "Получить аналитику окна")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные найдены",
                    content = @Content(schema = @Schema(implementation = WindowAnalyticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Окно не найдено")
    })
    public ResponseEntity<Optional<WindowAnalyticsDTO>> getWindowAnalytics(
            @Parameter(description = "ID окна", example = "window-123", required = true)
            @PathVariable String windowId) {
        return ResponseEntity.ok(analyticsService.getWindowAnalytics(windowId));
    }

    @GetMapping("/windows/status/{active}")
    @Operation(summary = "Получить окна по статусу")
    @ApiResponse(responseCode = "200", description = "Список окон",
            content = @Content(schema = @Schema(implementation = WindowAnalyticsDTO.class)))
    public ResponseEntity<List<WindowAnalyticsDTO>> getWindowsByStatus(
            @Parameter(description = "Статус активности", example = "true", required = true)
            @PathVariable boolean active) {
        return ResponseEntity.ok(analyticsService.getWindowsByStatus(active));
    }

    @PostMapping(value = "/users/logins", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Зарегистрировать вход пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вход зарегистрирован",
                    content = @Content(schema = @Schema(implementation = UserAnalyticsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные данные")
    })
    public ResponseEntity<LocalDateTime> recordUserLogin(
            @Parameter(description = "ID пользователя в формате JSON", required = true)
            @Valid @RequestBody String userId) {
        return ResponseEntity.ok(analyticsService.recordUserLogin(userId));
    }

    @GetMapping("/presets/{userId}")
    @Operation(summary = "Получить пресеты пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пресетов",
                    content = @Content(schema = @Schema(implementation = PresetAnalyticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<PresetAnalyticsDTO>> getUserPresets(
            @Parameter(description = "ID пользователя", example = "user-456", required = true)
            @PathVariable String userId) {
        return ResponseEntity.ok(analyticsService.getUserPresets(userId));
    }
}