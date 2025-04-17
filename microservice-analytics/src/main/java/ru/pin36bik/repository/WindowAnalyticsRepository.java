package ru.pin36bik.repository;

import ru.pin36bik.entity.WindowAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WindowAnalyticsRepository extends JpaRepository<WindowAnalytics, String> {
    List<WindowAnalytics> findByWindowId(String windowId);
    List<WindowAnalytics> findByActive(boolean isActive);
}
