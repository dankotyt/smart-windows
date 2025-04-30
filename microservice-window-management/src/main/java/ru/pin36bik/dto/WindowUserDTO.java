package ru.pin36bik.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class WindowUserDTO {
    private Long windowId;
    private String userEmail;
    private String name;
    private boolean status;
    private String presets;
}
