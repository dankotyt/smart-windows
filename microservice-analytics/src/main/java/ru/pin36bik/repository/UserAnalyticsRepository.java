package ru.pin36bik.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pin36bik.entity.UserAnalytics;

@Repository
@Table(name = "users_analytics")
public interface UserAnalyticsRepository
        extends JpaRepository<UserAnalytics, Long> {
    List<UserAnalytics> findByUserId(Long userId);

    @Query("SELECT u FROM User u ORDER BY u.lastLoginDate ASC LIMIT 1")
    Optional<UserAnalytics> findOldestLoggedInUser();

    @Modifying
    @Query("UPDATE UserAnalytics u SET u.loginCount = "
            + "u.loginCount + 1, "
            + "u.lastLogin = :now WHERE u.userId = :userId")
    void incrementLoginCount(@Param("userId") Long userId,
                             @Param("now") LocalDateTime now);
}
