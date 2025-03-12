package ru.pin36bik.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User - сущность, которая будет передаваться в БД.
 * Отличается от UserDTO только тем, что тут хранятся те данные,
 * которые пользователь не увидит.
 * */

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    //location по API

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Возвращает роли пользователя
    }

    @Override
    public String getUsername() {
        return email; // Используем email как username
    }

    @Override
    public String getPassword() {
        return password; // Возвращаем пароль
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Аккаунт не просрочен
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Аккаунт не заблокирован
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Пароль не просрочен
    }

    @Override
    public boolean isEnabled() {
        return true; // Аккаунт активен
    }
}
