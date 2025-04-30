package ru.pin36bik.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WindowResponse {
    private Long window_id;
    private String userEmail;
    private String name;
    private boolean status;
    private String presets;
}

