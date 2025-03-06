package model;

import lombok.Getter;
import lombok.Setter;

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
    private String email;
    private String password;

}
