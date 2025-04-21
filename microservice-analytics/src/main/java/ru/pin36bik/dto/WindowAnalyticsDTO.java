package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Данные о характеристиках и состоянии умного окна")
public class WindowAnalyticsDTO {
    @NotBlank
    @Schema(description = "Уникальный идентификатор окна",
            example = "window-123")
    @JsonProperty("window_id")
    private String windowId;

    @Schema(description = "Статус эксплуатации окна",
            example = "true")
    @JsonProperty("is_active")
    private boolean isActive;

    @Min(1)
    @Schema(description = "Количество створок",
            example = "2")
    @JsonProperty("sections_count")
    private int sectionsCount;

    @Positive
    @Schema(description = "Высота окна в метрах",
            example = "1.5")
    @JsonProperty("height")
    private double height;

    @Positive
    @Schema(description = "Размер створки в метрах",
            example = "0.75")
    @JsonProperty("section_size")
    private double sectionSize;

    @Schema(description = "Временная метка данных",
            example = "2023-12-01T10:15:30")
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public WindowAnalyticsDTO() {
    }

    public WindowAnalyticsDTO(final String myWindowId,
                              final boolean myIsActive,
                              final int mySectionsCount,
                              final double myHeight,
                              final double mySectionSize,
                              final LocalDateTime myTimestamp) {
        this.windowId = myWindowId;
        this.isActive = myIsActive;
        this.sectionsCount = mySectionsCount;
        this.height = myHeight;
        this.sectionSize = mySectionSize;
        this.timestamp = myTimestamp;
    }
}
