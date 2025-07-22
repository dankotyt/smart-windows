package ru.pin36bik.repository;

import jakarta.annotation.Nullable;
import ru.pin36bik.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Nullable
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    Optional<User> findByRefreshToken(String token);
}
