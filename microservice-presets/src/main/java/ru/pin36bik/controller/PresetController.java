package ru.pin36bik.controller;

import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.service.PresetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/presets")
@Tag(name = "Preset Controller", description = "API для управления пресетами настроек окна")
public class PresetController {

    private final PresetService presetService;

    @Autowired
    public PresetController(PresetService presetService) {
        this.presetService = presetService;
    }

    @Operation(summary = "Найти пресет по его ID", description = "Возвращает единственный пресет по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пресет успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пресет не найден")
    })
    @GetMapping("/get/{id}")
    public PresetDTO getPreset(
            @Parameter(description = "ID нужного пресета", required = true)
            @PathVariable Long id) {
        return presetService.getPresetById(id);
    }

    @Operation(summary = "Создать новый пресет", description = "Возвращает вновь созданный пресет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пресет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные")
    })
    @PostMapping("/post")
    public PresetDTO createPreset(
            @Parameter(description = "DTO пресета, который будет создан", required = true)
            @Valid @RequestBody PresetDTO presetDTO) {
        return presetService.createPreset(presetDTO);
    }

    @Operation(summary = "Обновить существующий пресет", description = "Обновляет существующий пресет и возвращает его")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пресет успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные"),
            @ApiResponse(responseCode = "404", description = "Пресет не найден")
    })
    @PutMapping("/update")
    public PresetDTO updatePreset(
            @Parameter(description = "DTO пресета, который будет обновлён", required = true)
            @Valid @RequestBody PresetDTO presetDTO) {
        return presetService.updatePreset(presetDTO);
    }

    @Operation(summary = "Удалить пресет по его ID", description = "Удаляет пресет с заданным ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пресет успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пресет не найден")
    })
    @DeleteMapping("/delete/{id}")
    public void deletePreset(
            @Parameter(description = "ID пресета, который будет удалён", required = true)
            @PathVariable Long id) {
        presetService.deletePreset(id);
    }
}