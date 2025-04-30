package ru.pin36bik.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.pin36bik.dto.WindowRequest;
import ru.pin36bik.dto.WindowResponse;
import ru.pin36bik.dto.WindowUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.exceptions.InvalidTokenException;
import ru.pin36bik.service.WindowService;

import java.util.List;

@RestController
@RequestMapping("/api/windows")
@RequiredArgsConstructor
@Slf4j
public class WindowController {
    private final WindowService windowService;

    @PostMapping("/add")
    public ResponseEntity<WindowResponse> addUserWindow(
            @RequestBody WindowRequest createRequest,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken) {

        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }

        log.info("Received request from user: " + userEmail);
        return ResponseEntity.ok(windowService.addWindow(createRequest, userEmail));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<WindowUserDTO>> getWindowsByUserEmail(@PathVariable String userEmail) {
        return ResponseEntity.ok(windowService.findByUserEmail(userEmail));
    }
}
