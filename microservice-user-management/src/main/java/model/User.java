package model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * User - сущность, которая будет передаваться в БД.
 * Отличается от UserDTO только тем, что тут хранятся те данные,
 * которые пользователь не увидит.
 * */
@Data
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDateTime createdAt;

}
