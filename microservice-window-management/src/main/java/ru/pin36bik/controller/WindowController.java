package ru.pin36bik.controller;

import ru.pin36bik.dto.WindowCreateRequest;
import ru.pin36bik.dto.WindowResponse;
import ru.pin36bik.dto.WindowUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.service.WindowService;

import java.util.List;

@RestController
@RequestMapping("/api/windows")
public class WindowController {
    private WindowService windowService;

    @PostMapping("/add")
    public ResponseEntity<WindowResponse> addUserWindow(@RequestBody WindowCreateRequest createRequest) {
        return ResponseEntity.ok(windowService.addWindow(createRequest));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<WindowUserDTO>> getWindowsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(windowService.findByUserId(id));
    }
}
