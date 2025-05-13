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
@RequestMapping("/api/v1/windows")
@RequiredArgsConstructor
@Slf4j
public class WindowController {
    private final WindowService windowService;

    @PostMapping("/add")
    public ResponseEntity<WindowResponse> addWindow(
            @RequestBody WindowRequest createRequest,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken) {

        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }

        log.info("Received request from user: " + userEmail);
        return ResponseEntity.ok(windowService.addWindow(createRequest, userEmail));
    }

    @PatchMapping("/update/{windowId}")
    public ResponseEntity<WindowResponse> updateWindow(
            @RequestBody WindowRequest request,
            @PathVariable Long windowId,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken) {
        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }

        return ResponseEntity.ok(windowService.updateWindow(request, userEmail, windowId));
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<WindowUserDTO>> getWindowsByUserEmail(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken) {
        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }
        return ResponseEntity.ok(windowService.findByUserEmail(userEmail));
    }

    @DeleteMapping("/delete/{windowId}")
    public ResponseEntity<WindowResponse> deleteWindow(
            @PathVariable Long windowId,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Valid-Token") String validToken) {
        if (!"true".equals(validToken)) {
            throw new InvalidTokenException("Token validation failed");
        }
        windowService.deleteWindow(windowId, userEmail);
        return ResponseEntity.noContent().build();
    }
}
