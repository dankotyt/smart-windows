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
import ru.pin36bik.service.AnalyticsService;

@RestController
@RequestMapping(value = "/api/v0/analytics",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Window Analytics API",
        description = "API для работы с данными об использовании умных окон")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(final AnalyticsService myAnalyticsService) {
        this.analyticsService = myAnalyticsService;
    }

    @PostMapping(value = "/presets",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создать новый пресет")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Пресет успешно создан",
                    content = @Content(schema =
                    @Schema(implementation = PresetAnalyticsDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные пресета")
    })
    public ResponseEntity<?> createPreset(
            @Parameter(description = "Данные пресета в формате JSON",
                    required = true)
            @Valid @RequestBody final PresetAnalyticsDTO presetAnalyticsDTO) {
        if (presetAnalyticsDTO.getPresetName() == null
                || presetAnalyticsDTO.getPresetName().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    "Preset name cannot be empty");
        }
        return ResponseEntity.ok(
                analyticsService.savePresetAnalytics(presetAnalyticsDTO));
    }

    @GetMapping("/presets/get-by-name/{preset-name}")
    @Operation(summary = "Найти пресет по его названию",
            description = "Возвращает пресет по указанному названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пресет успешно получен"),
            @ApiResponse(responseCode = "404",
                    description = "Пресет не найден")
    })
    public ResponseEntity<PresetAnalyticsDTO> getPresetByName(
            @Parameter(description = "Название пресета",
                    required = true, example = "test-preset")
            @PathVariable("preset-name") final String presetName) {
        Optional<PresetAnalyticsDTO> preset = analyticsService.
                getPresetAnalytics(presetName);
        return preset.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/presets/downloads",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Зарегистрировать загрузку "
            + "пресета с заданным названием")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Загрузка зарегистрирована",
                    content = @Content(schema = @Schema(
                            implementation = PresetAnalyticsDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные!")
    })
    public ResponseEntity<LocalDateTime> recordPresetDownload(
            @Parameter(description = "Название пресета в формате JSON",
                    required = true, example = "test-preset")
            @Valid @RequestBody final String presetName) {
        return ResponseEntity.ok(
                analyticsService.recordPresetDownload(presetName));
    }

    @GetMapping("/presets/most-downloaded")
    @Operation(summary =
            "Получить самый популярный пресет (по количеству скачиваний)")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Пресет найден",
                    content = @Content(schema =
                    @Schema(implementation = PresetAnalyticsDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Пресеты не найдены")
    })
    public ResponseEntity<PresetAnalyticsDTO> getMostDownloadedPreset() {
        return analyticsService.getMostDownloadedPreset()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создать нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Пресет успешно создан",
                    content = @Content(schema =
                    @Schema(implementation = PresetAnalyticsDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные пресета")
    })
    public ResponseEntity<UserAnalyticsDTO> createUser(
            @Parameter(description = "Данные пресета в формате JSON",
                    required = true)
            @Valid @RequestBody final UserAnalyticsDTO userAnalyticsDTO) {
        return ResponseEntity.ok(analyticsService
                .saveUserAnalytics(userAnalyticsDTO));
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
                    required = true, example = "12345")
            @Valid @RequestBody final Long userId) {
        return ResponseEntity.ok(analyticsService.recordUserLogin(userId));
    }

    @GetMapping("/users/get-by-id/{user-id}")
    @Operation(summary = "Найти пользователя по его ID",
            description = "Возвращает запись о пользователе по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пользователь успешно получен"),
            @ApiResponse(responseCode = "404",
                    description = "Пользователь не найден")
    })
    public ResponseEntity<UserAnalyticsDTO> getUserById(
            @Parameter(description = "Идентификатор пользователя",
                    required = true, example = "12345")
            @PathVariable("user-id") final Long userId) {
        Optional<UserAnalyticsDTO> user = analyticsService.
                getUserAnalytics(userId);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/earliest-login")
    @Operation(summary = "Получить пользователя с самым старым логином")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(schema =
                    @Schema(implementation = UserAnalyticsDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Пользователи не найдены")
    })
    public ResponseEntity<UserAnalyticsDTO> getUserWithEarliestLogin() {
        return analyticsService.getUserWithEarliestLogin()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
