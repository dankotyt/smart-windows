package ru.pin36bik.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pin36bik.dto.WindowLocationDTO;
import ru.pin36bik.entity.WindowUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WindowRepository extends JpaRepository<WindowUser, Long> {
    List<WindowUser> findWindowsByUserEmail(String userEmail);
    Optional<WindowUser> findByWindowId(Long windowId);
    boolean existsByUserEmailAndWindowId(String userEmail, Long windowId);
}
