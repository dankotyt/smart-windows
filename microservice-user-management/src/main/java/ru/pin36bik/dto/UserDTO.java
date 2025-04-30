package ru.pin36bik.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDTO - сущность, которая передает данные между контроллером
 * и клиентом. Тут находятся те данные, которые пользователь
 * сможет увидеть.
 * */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("birthday")
    private LocalDate birthday;

    @Email(message = "Некорректный email!")
    @JsonProperty("email")
    private String email;
}
