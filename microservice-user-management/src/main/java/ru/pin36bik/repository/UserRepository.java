package ru.pin36bik.repository;

import ru.pin36bik.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    boolean existsByEmail(String email);
    Optional<User> findByRefreshToken(String token);
}
