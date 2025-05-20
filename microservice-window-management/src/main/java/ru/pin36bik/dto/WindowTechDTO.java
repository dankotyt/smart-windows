package ru.pin36bik.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class WindowTechDTO {
    private Long id;
    private Long windowId;
    private boolean status;
    private double width;
    private double height;
    private String material;
    private String manufacturer;
}
