package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Данные о пользовательском пресете")
public class PresetAnalyticsDTO {
    @Schema(description = "ID записи", example = "1")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "ID пользователя", example = "user-456")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "Название пресета", example = "Утренний режим")
    @JsonProperty("preset_name")
    private String presetName;

    @Schema(description = "Время загрузки пресета", example = "2023-12-01T10:15:30")
    @JsonProperty("downloaded_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime downloadedAt;

    public PresetAnalyticsDTO() {
    }

    public PresetAnalyticsDTO(Long id, String userId, String presetName, LocalDateTime downloadedAt) {
        this.id = id;
        this.userId = userId;
        this.presetName = presetName;
        this.downloadedAt = downloadedAt;
    }
}
