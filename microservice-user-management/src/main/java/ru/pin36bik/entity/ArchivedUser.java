package ru.pin36bik.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "archived_users")
public class ArchivedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String lastName;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime deletedAt;
}
