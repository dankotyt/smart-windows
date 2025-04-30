package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Данные о пресете настроек окна, "
        + "доступном для загрузки")
public class PresetAnalyticsDTO {
    @Schema(description = "ID пресета",
            example = "12345")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Количество загрузок данного пресета",
            example = "456000")
    @JsonProperty("downloads_number")
    private Long downloadsNumber;

    @Schema(description = "Название пресета",
            example = "Утренний режим")
    @JsonProperty("preset_name")
    private String presetName;

    @Schema(description = "Время создания пресета",
            example = "2023-12-01T10:15:30")
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public PresetAnalyticsDTO() {
    }

    public PresetAnalyticsDTO(final Long myId,
                              final Long myDownloadsNumber,
                              final String myPresetName,
                              final LocalDateTime myCreatedAt) {
        this.id = myId;
        this.downloadsNumber = myDownloadsNumber;
        this.presetName = myPresetName;
        this.createdAt = myCreatedAt;
    }
}
