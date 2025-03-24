package ru.pin36bik.repository;

import org.springframework.stereotype.Repository;
import ru.pin36bik.entity.WindowUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface WindowRepository extends JpaRepository<WindowUser, Long> {
    Optional<WindowUser> findByWindowId(Long windowId);
}
