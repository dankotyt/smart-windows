package ru.pin36bik.repository;

import ru.pin36bik.entity.UserAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface UserAnalyticsRepository extends JpaRepository<UserAnalytics, String> {
    List<UserAnalytics> findByUserId(String userId);

    @Modifying
    @Query("UPDATE UserAnalytics u SET u.loginCount = u.loginCount + 1, u.lastLogin = :now WHERE u.userId = :userId")
    void incrementLoginCount(@Param("userId") String userId, @Param("now") LocalDateTime now);
}
