package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Данные о пользователе")
public class UserAnalyticsDTO {
    @Schema(description = "Уникальный идентификатор пользователя",
            example = "user-456")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "Количество входов в систему",
            example = "5")
    @JsonProperty("login_count")
    private int loginCount;

    @Schema(description = "Время последнего входа",
            example = "2023-12-01T10:15:30")
    @JsonProperty("last_login")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLogin;

    @Schema(description = "Список пресетов, загруженных пользователем")
    @JsonProperty("preset_names")
    private List<String> presetNames;

    @Schema(description = "Временная метка данных",
            example = "2023-12-01T10:15:30")
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public UserAnalyticsDTO() {
    }

    public UserAnalyticsDTO(final String myUserId,
                            final int myLoginCount,
                            final LocalDateTime myLastLogin,
                            final List<String> myPresetNames,
                            final LocalDateTime myTimestamp) {
        this.userId = myUserId;
        this.loginCount = myLoginCount;
        this.lastLogin = myLastLogin;
        this.presetNames = myPresetNames;
        this.timestamp = myTimestamp;
    }
}
