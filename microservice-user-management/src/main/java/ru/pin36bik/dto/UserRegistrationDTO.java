package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Getter
@Setter
public class UserRegistrationDTO {

    @NotNull(message = "Поле не может быть пустым!")
    private String name;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Email(message = "Некорректный email!")
    private String email;

    private String password;
    private LocalDateTime registrationDate;
}
