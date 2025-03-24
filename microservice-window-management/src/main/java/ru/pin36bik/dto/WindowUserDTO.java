package ru.pin36bik.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class WindowUserDTO {
    private Long id;
    private Long windowId;
    private Long userId;
    private String name;
    private boolean status;
    private String presets;
}
