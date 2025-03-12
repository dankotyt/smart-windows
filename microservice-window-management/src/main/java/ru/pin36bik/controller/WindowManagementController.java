package ru.pin36bik.controller;

import ru.pin36bik.model.WindowManagementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.service.WindowManagementService;

@RestController
@RequestMapping("api/v1/windows")
public class WindowManagementController {

    private final WindowManagementService windowManagementService;

    @Autowired
    public WindowManagementController(WindowManagementService windowManagementService) {
        this.windowManagementService = windowManagementService;
    }

    @PostMapping("/add-window")
    public ResponseEntity<WindowManagementDTO> addWindow(@RequestHeader() String token,
                                                         @RequestBody AddW) {

    }

}
