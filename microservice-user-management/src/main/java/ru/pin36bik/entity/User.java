package ru.pin36bik.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
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
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(updatable = false, unique = true, name = "id")
    private UUID userId;

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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "refresh_token_expiry")
    private LocalDateTime refreshTokenExpiry;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
    //location по API

}
