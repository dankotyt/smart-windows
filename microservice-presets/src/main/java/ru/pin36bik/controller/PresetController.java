package ru.pin36bik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.service.PresetService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/presets",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Preset Controller", description = "API для управления пресетами настроек окна")
public class PresetController {

    private final PresetService presetService;

    @Autowired
    public PresetController(PresetService presetService) {
        this.presetService = presetService;
    }

    @Operation(summary = "Найти пресет по ID", description = "Возвращает пресет по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пресет успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пресет не найден")
    })
    @GetMapping("/{id}")
    public PresetDTO getPreset(
            @Parameter(description = "ID пресета", required = true, example = "1")
            @PathVariable Long id) {
        return presetService.getPresetById(id);
    }

    @Operation(summary = "Создать новый пресет", description = "Создает и возвращает новый пресет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пресет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public PresetDTO createPreset(
            @Parameter(description = "Данные пресета в формате JSON", required = true)
            @Valid @RequestBody PresetDTO presetDTO) {
        return presetService.createPreset(presetDTO);
    }

    @Operation(summary = "Обновить пресет", description = "Обновляет и возвращает существующий пресет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пресет успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пресет не найден")
    })
    @PutMapping("/{id}")
    public PresetDTO updatePreset(
            @Parameter(description = "ID пресета", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные пресета", required = true)
            @Valid @RequestBody PresetDTO presetDTO) {
        return presetService.updatePreset(presetDTO);
    }

    @Operation(summary = "Удалить пресет", description = "Удаляет пресет по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пресет успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пресет не найден")
    })
    @DeleteMapping("/{id}")
    public void deletePreset(
            @Parameter(description = "ID пресета", required = true, example = "1")
            @PathVariable Long id) {
        presetService.deletePreset(id);
    }
}