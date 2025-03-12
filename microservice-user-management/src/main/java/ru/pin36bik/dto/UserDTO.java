package ru.pin36bik.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;

/**
 * UserDTO - сущность, которая передает данные между контроллером
 * и клиентом. Тут находятся те данные, которые пользователь
 * сможет увидеть.
 * */
@Data
public class UserDTO {
    private String name;
    private String lastName;
    private LocalDate birthday;

    @Email(message = "Invalid email format!")
    private String email;
}
