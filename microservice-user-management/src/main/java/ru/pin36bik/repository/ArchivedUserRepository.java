package ru.pin36bik.repository;

import ru.pin36bik.entity.ArchivedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchivedUserRepository extends JpaRepository<ArchivedUser, Long> {
    Optional<ArchivedUser> findByEmail(String email);
}
