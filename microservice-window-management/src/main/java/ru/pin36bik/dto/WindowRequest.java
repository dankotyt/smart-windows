package ru.pin36bik.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WindowRequest {
    private Long windowId;
    private String name;
}
