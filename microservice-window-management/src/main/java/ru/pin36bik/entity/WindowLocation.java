package ru.pin36bik.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class WindowLocation {
    @Column(name = "city")
    private String cityName;

    @Column(name = "lat")
    private Double latitude;

    @Column(name = "lng")
    private Double longitude;
}
