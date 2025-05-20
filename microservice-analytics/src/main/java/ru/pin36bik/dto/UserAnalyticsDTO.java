package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "Данные о пользователе")
public class UserAnalyticsDTO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.
            ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Schema(description = "Уникальный идентификатор пользователя",
            example = "456")
    @JsonProperty("user_id")
    private Long userId;

    @Schema(description = "Количество входов в систему",
            example = "5")
    @JsonProperty("login_count")
    private int loginCount;

    @Schema(description = "Время последнего входа",
            example = "2023-12-01T10:15:30")
    @JsonProperty("last_login")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLogin;

    @Schema(description = "Временная метка данных",
            example = "2023-12-01T10:15:30")
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public UserAnalyticsDTO() {
    }

    public UserAnalyticsDTO(final Long myUserId,
                            final int myLoginCount,
                            final LocalDateTime myLastLogin,
                            final LocalDateTime myTimestamp) {
        this.userId = myUserId;
        this.loginCount = myLoginCount;
        this.lastLogin = myLastLogin;
        this.timestamp = myTimestamp;
    }

    @Override
    public String toString() {
        return "UserAnalyticsDTO("
                + "userId=" + userId
                + ", loginCount=" + loginCount
                + ", lastLogin=" + (lastLogin != null ? lastLogin
                .format(FORMATTER) : null)
                + ", timestamp=" + (timestamp != null ? timestamp
                .format(FORMATTER) : null)
                + ')';
    }
}
