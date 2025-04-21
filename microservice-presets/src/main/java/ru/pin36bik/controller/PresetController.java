package ru.pin36bik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.service.PresetService;

@RestController
@RequestMapping(value = "/api/presets/v0",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Preset Controller",
        description = "API для управления пресетами настроек окна")
public class PresetController {

    private final PresetService presetService;
    private final KafkaTemplate<String, PresetDTO> kafkaTemplate;

    @Autowired
    public PresetController(final PresetService myPresetService,
                            final KafkaTemplate<String,
                                    PresetDTO> myKafkaTemplate) {
        this.presetService = myPresetService;
        this.kafkaTemplate = myKafkaTemplate;
    }

    @Operation(summary = "Найти пресет по ID",
            description = "Возвращает пресет по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пресет успешно получен"),
            @ApiResponse(responseCode = "404",
                    description = "Пресет не найден")
    })
    @GetMapping("/get-by-id/{id}")
    public PresetDTO getPresetById(
            @Parameter(description = "ID пресета",
                    required = true, example = "1")
            @PathVariable final Long id) {
        return presetService.getPresetById(id);
    }

    @Operation(summary = "Создать новый пресет",
            description = "Создает и возвращает новый пресет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пресет успешно создан"),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные")
    })
    @PostMapping("/create")
    public PresetDTO createPreset(
            @Parameter(description = "Данные пресета в формате JSON",
                    required = true)
            @Valid @RequestBody final PresetDTO preset_DTO) {
        return presetService.createPreset(preset_DTO);
    }

    @Operation(summary = "Обновить пресет",
            description = "Обновляет и возвращает существующий пресет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пресет успешно обновлен"),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные"),
            @ApiResponse(responseCode = "404",
                    description = "Пресет не найден")
    })
    @PutMapping("/update-by-id/{id}")
    public PresetDTO updatePresetById(
            @Parameter(description = "ID пресета",
                    required = true,
                    example = "1")
            @PathVariable final Long id,
            @Parameter(description = "Обновленные данные пресета",
                    required = true)
            @Valid @RequestBody final PresetDTO preset_DTO) {
        return presetService.updatePreset(preset_DTO);
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
    public void deletePresetById(
            @Parameter(description = "ID пресета",
                    required = true,
                    example = "1")
            @PathVariable final Long id) {
        presetService.deletePreset(id);
    }
}
