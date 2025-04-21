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
        + "загруженном пользователем")
public class PresetAnalyticsDTO {
    @Schema(description = "ID пресета",
            example = "1")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "ID пользователя, загрузившего пресет",
            example = "user-456")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "Название пресета",
            example = "Утренний режим")
    @JsonProperty("preset_name")
    private String presetName;

    @Schema(description = "Время загрузки пресета",
            example = "2023-12-01T10:15:30")
    @JsonProperty("downloaded_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime downloadedAt;

    public PresetAnalyticsDTO() {
    }

    public PresetAnalyticsDTO(final Long myId,
                              final String myUserId,
                              final String myPresetName,
                              final LocalDateTime myDownloadedAt) {
        this.id = myId;
        this.userId = myUserId;
        this.presetName = myPresetName;
        this.downloadedAt = myDownloadedAt;
    }
}
