package ru.pin36bik.controller;

import ru.pin36bik.dto.WindowUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.service.WindowService;

@RestController
@RequestMapping("api/v1/windows")
public class WindowController {

    private final WindowService windowService;

    @Autowired
    public WindowController(WindowService windowService) {
        this.windowService = windowService;
    }

//    @PostMapping("/add-window")
//    public ResponseEntity<WindowUserDTO> addWindow(@RequestHeader() String token,
//                                                   @RequestBody AddW) {
//
//    }

}
