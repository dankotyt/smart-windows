package ru.pin36bik.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserLoginDTO {

    @Email(message = "Некорректный email!")
    private String email;
    private String password;
}
