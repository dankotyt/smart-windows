package ru.pin36bik.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WindowUserDTO {
    private Long windowId;
    private String userEmail;
    private String name;
    private boolean status;
    private String presets;
    private WindowLocationDTO location;
}
