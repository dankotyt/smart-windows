package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Данные аналитики для умного окна")
public class WindowAnalyticsDTO {
    @NotBlank
    @Schema(description = "Уникальный идентификатор окна", example = "window-123")
    @JsonProperty("window_id")
    private String windowId;

    @Schema(description = "Статус активности окна", example = "true")
    @JsonProperty("is_active")
    private boolean isActive;

    @Min(1)
    @Schema(description = "Количество створок", example = "2")
    @JsonProperty("sections_count")
    private int sectionsCount;

    @Positive
    @Schema(description = "Высота окна в метрах", example = "1.5")
    @JsonProperty("height")
    private double height;

    @Positive
    @Schema(description = "Размер створки в метрах", example = "0.75")
    @JsonProperty("section_size")
    private double sectionSize;

    @Schema(description = "Временная метка события", example = "2023-12-01T10:15:30")
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public WindowAnalyticsDTO() {
    }

    public WindowAnalyticsDTO(String windowId, boolean isActive, int sectionsCount, double height,
                              double sectionSize, LocalDateTime timestamp) {
        this.windowId = windowId;
        this.isActive = isActive;
        this.sectionsCount = sectionsCount;
        this.height = height;
        this.sectionSize = sectionSize;
        this.timestamp = timestamp;
    }
}