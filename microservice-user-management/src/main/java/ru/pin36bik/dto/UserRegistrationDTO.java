package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserRegistrationDTO {

    @NotNull(message = "Name could not be null!")
    private String name;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Email(message = "Invalid email format!")
    private String email;

    private String password;
    private LocalDateTime registrationDate;
}
