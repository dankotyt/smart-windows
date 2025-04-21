package ru.pin36bik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pin36bik.dto.PresetAnalyticsDTO;
import ru.pin36bik.dto.UserAnalyticsDTO;
import ru.pin36bik.dto.WindowAnalyticsDTO;
import ru.pin36bik.service.AnalyticsService;

@RestController
@RequestMapping(value = "/api/analytics",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Window Analytics API",
        description = "API для работы с данными об использовании умных окон")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(final AnalyticsService myAnalyticsService) {
        this.analyticsService = myAnalyticsService;
    }

    @PostMapping(value = "/windows",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Сохранить полученные данные окна")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Данные сохранены",
                    content = @Content(schema = @Schema(
                            implementation = WindowAnalyticsDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные!")
    })
    public ResponseEntity<WindowAnalyticsDTO> saveWindowAnalytics(
            @Parameter(description = "Данные окна в формате JSON",
                    required = true)
            @Valid @RequestBody final WindowAnalyticsDTO dto) {
        return ResponseEntity.ok(analyticsService.saveWindowAnalytics(dto));
    }

    @GetMapping("/windows/{windowId}")
    @Operation(summary = "Получить данные об окне по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Данные успешно найдены",
                    content = @Content(schema = @Schema(
                            implementation = WindowAnalyticsDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Окно не найдено")
    })
    public ResponseEntity<Optional<WindowAnalyticsDTO>> getWindowAnalytics(
            @Parameter(description = "ID окна",
                    example = "window-123", required = true)
            @PathVariable final String windowId) {
        return ResponseEntity.ok(analyticsService.getWindowAnalytics(windowId));
    }

    @GetMapping("/windows/status/{active}")
    @Operation(summary = "Получить список окон в определённом состоянии")
    @ApiResponse(responseCode = "200",
            description = "Список окон",
            content = @Content(schema = @Schema(
                    implementation = WindowAnalyticsDTO.class)))
    public ResponseEntity<List<WindowAnalyticsDTO>> getWindowsByStatus(
            @Parameter(description = "Эксплуатируется/не эксплуатируется",
                    example = "true",
                    required = true)
            @PathVariable final boolean active) {
        return ResponseEntity.ok(analyticsService.getWindowsByStatus(active));
    }

    @PostMapping(value = "/users/logins",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Зарегистрировать вход "
            + "пользователя с заданным ID в систему")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Вход зарегистрирован",
                    content = @Content(schema = @Schema(
                            implementation = UserAnalyticsDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные!")
    })
    public ResponseEntity<LocalDateTime> recordUserLogin(
            @Parameter(description = "ID пользователя в формате JSON",
                    required = true)
            @Valid @RequestBody final String userId) {
        return ResponseEntity.ok(analyticsService.recordUserLogin(userId));
    }

    @GetMapping("/presets/{userId}")
    @Operation(summary = "Получить пресеты, загруженные пользователем")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Список пресетов",
                    content = @Content(schema = @Schema(
                            implementation = PresetAnalyticsDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Пользователь не найден!")
    })
    public ResponseEntity<List<PresetAnalyticsDTO>> getUserPresets(
            @Parameter(description = "ID пользователя",
                    example = "user-456", required = true)
            @PathVariable final String userId) {
        return ResponseEntity.ok(analyticsService.getUserPresets(userId));
    }
}
