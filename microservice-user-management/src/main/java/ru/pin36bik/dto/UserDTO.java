package ru.pin36bik.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * UserDTO - сущность, которая передает данные между контроллером
 * и клиентом. Тут находятся те данные, которые пользователь
 * сможет увидеть.
 * */
@Getter
@Setter
public class UserDTO {
    private String name;
    private String lastName;
    private LocalDate birthday;

    @Email(message = "Некорректный email!")
    private String email;
}
