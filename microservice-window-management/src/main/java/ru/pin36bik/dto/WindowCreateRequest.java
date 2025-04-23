package ru.pin36bik.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WindowCreateRequest {
    Long windowId;
    String name;
    Long userId;
}
