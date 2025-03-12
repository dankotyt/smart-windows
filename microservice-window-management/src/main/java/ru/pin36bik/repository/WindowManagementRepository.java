package ru.pin36bik.repository;

import ru.pin36bik.model.WindowManagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WindowManagementRepository extends JpaRepository<WindowManagement, Long> {
    Optional<WindowManagement> findByWindowId(String id);
}
