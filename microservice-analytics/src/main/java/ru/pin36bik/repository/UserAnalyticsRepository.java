package ru.pin36bik.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pin36bik.entity.UserAnalytics;

@Repository
public interface UserAnalyticsRepository
        extends JpaRepository<UserAnalytics, Long> {
    List<UserAnalytics> findByUserId(Long userId);

    @Query("SELECT u FROM UserAnalytics u ORDER BY u.lastLogin ASC LIMIT 1")
    Optional<UserAnalytics> findOldestLoggedInUser();

    @Modifying
    @Query("UPDATE UserAnalytics u SET u.loginCount = "
            + "u.loginCount + 1, "
            + "u.lastLogin = :now WHERE u.userId = :userId")
    void incrementLoginCount(@Param("userId") Long userId,
                             @Param("now") LocalDateTime now);
}
