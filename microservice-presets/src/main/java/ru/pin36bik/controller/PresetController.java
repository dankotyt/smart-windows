package ru.pin36bik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.exceptions.PresetNotFoundException;
import ru.pin36bik.service.PresetService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/presets",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Preset API",
        description = "API для управления пресетами настроек окна")
@Slf4j
public class PresetController {

    private final PresetService presetService;
    private final KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @Operation(summary = "Получить пресет по ID",
            description = "Возвращает пресет по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пресет успешно получен"),
            @ApiResponse(responseCode = "404",
                    description = "Пресет не найден")
    })
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<PresetDTO> getPresetById(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken,
            @Parameter(description = "ID пресета",
                    required = true, example = "1")
            @PathVariable final Long id) {
        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }

        log.info("Received request on getting preset from user: " + userEmail);
        try {
            PresetDTO presetDTO = presetService.getPresetById(id);
            return ResponseEntity.ok(presetDTO);
        } catch (PresetNotFoundException ex) {
            throw ex;
        }
    }

    @Operation(summary = "Создать новый пресет",
            description = "Создает и возвращает новый пресет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Пресет успешно создан"),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные")
    })
    @PostMapping("/create")
    public ResponseEntity<PresetDTO> createPreset(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken,
            @Parameter(description = "Данные пресета в формате JSON",
                    required = true)
            @Valid @RequestBody final PresetDTO presetDTO) {
        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }

        log.info("Received request on creating preset from user: " + userEmail);
        return ResponseEntity.ok(presetService.createPreset(presetDTO));
    }

    @Operation(summary = "Обновить пресет",
            description = "Обновляет и возвращает существующий пресет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пресет успешно обновлен"),
            @ApiResponse(responseCode = "404",
                    description = "Пресет не найден"),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные")
    })
    @PutMapping("/update-by-id/{id}")
    public ResponseEntity<PresetDTO> updatePresetById(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken,
            @Parameter(description = "ID пресета",
                    required = true,
                    example = "1")
            @PathVariable final Long id,
            @Parameter(description = "Обновленные данные пресета",
                    required = true)
            @Valid @RequestBody final PresetDTO presetDTO) {
        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }

        log.info("Received request on updating preset from user: " + userEmail);
        return ResponseEntity.ok(presetService.updatePreset(presetDTO));
    }

    @Operation(summary = "Удалить пресет",
            description = "Удаляет пресет по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Пресет успешно удален"),
            @ApiResponse(responseCode = "404",
                    description = "Пресет не найден")
    })
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deletePresetById(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken,
            @Parameter(description = "ID пресета",
                    required = true,
                    example = "1")
            @PathVariable final Long id) {
        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }

        log.info("Received request on deleting preset from user: " + userEmail);
        presetService.deletePreset(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(PresetNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePresetNotFound(
            final PresetNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "NOT_FOUND");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
