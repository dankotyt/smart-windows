package ru.pin36bik.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Embeddable
@NoArgsConstructor
public class WindowLocationDTO {
    private String cityName;
    private Double latitude;
    private Double longitude;
}
