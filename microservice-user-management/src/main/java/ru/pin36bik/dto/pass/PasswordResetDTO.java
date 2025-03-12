package ru.pin36bik.dto.pass;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String token;
    private String newPassword;
}
