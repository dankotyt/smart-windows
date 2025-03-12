package ru.pin36bik.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserLoginDTO {

    @Email(message = "Invalid email format!")
    private String email;
    private String password;
}
