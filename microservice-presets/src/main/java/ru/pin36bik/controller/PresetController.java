package ru.pin36bik.controller;

import ru.pin36bik.dto.PresetDTO;
import ru.pin36bik.service.PresetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/presets")
public class PresetController {

    private final PresetService presetService;

    @Autowired
    public PresetController(PresetService presetService) {
        this.presetService = presetService;
    }

    @GetMapping("/get/{id}")
    public PresetDTO getPreset(@PathVariable Long id) {
        return presetService.getPresetById(id);
    }

    @PostMapping("/post")
    public PresetDTO createPreset(@Valid @RequestBody PresetDTO presetDTO) {
        return presetService.createPreset(presetDTO);
    }

    @PutMapping("/update")
    public PresetDTO updatePreset(@Valid @RequestBody PresetDTO presetDTO) {
        return presetService.updatePreset(presetDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePreset(@PathVariable Long id) {
        presetService.deletePreset(id);
    }
}