package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "Данные аналитики пользователя")
public class UserAnalyticsDTO {
    @Schema(description = "Уникальный идентификатор пользователя", example = "user-456")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "Количество входов", example = "5")
    @JsonProperty("login_count")
    private int loginCount;

    @Schema(description = "Время последнего входа", example = "2023-12-01T10:15:30")
    @JsonProperty("last_login")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLogin;

    @Schema(description = "Список пресетов пользователя")
    @JsonProperty("preset_names")
    private List<String> presetNames;

    @Schema(description = "Временная метка события", example = "2023-12-01T10:15:30")
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public UserAnalyticsDTO() {
    }

    public UserAnalyticsDTO(String userId, int loginCount, LocalDateTime lastLogin,
                            List<String> presetNames, LocalDateTime timestamp) {
        this.userId = userId;
        this.loginCount = loginCount;
        this.lastLogin = lastLogin;
        this.presetNames = presetNames;
        this.timestamp = timestamp;
    }
}