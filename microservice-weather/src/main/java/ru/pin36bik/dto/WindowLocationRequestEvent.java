package ru.pin36bik.dto;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WindowLocationRequestEvent {
    private String requestId; // UUID для корреляции
    private Long windowId;
    private String userEmail;
    private Instant timestamp;
}
