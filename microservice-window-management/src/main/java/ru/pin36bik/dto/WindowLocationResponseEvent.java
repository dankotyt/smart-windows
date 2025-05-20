package ru.pin36bik.dto;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WindowLocationResponseEvent {
    private String requestId;
    private Long windowId;
    private WindowLocationDTO location;
    private Instant timestamp;
}
