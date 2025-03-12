package ru.pin36bik.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class WindowManagementDTO {
    private String windowId;
    private String windowName;
    private String status;
    private String location;
}
