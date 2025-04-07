package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class RegistrationRequest {

    @NotNull(message = "Поле не может быть пустым!")
    private String name;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate birthday;

    @Email(message = "Некорректный email!")
    private String email;

    @Size(min=4, max=64)
    private String password;
    private LocalDateTime registrationDate;
}
