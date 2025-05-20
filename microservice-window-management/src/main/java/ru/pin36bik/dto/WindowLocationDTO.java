package ru.pin36bik.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WindowLocationDTO {
    private String cityName;
    private Double latitude;
    private Double longitude;
}
