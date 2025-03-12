package ru.pin36bik.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class WindowManagement {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String windowId;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
