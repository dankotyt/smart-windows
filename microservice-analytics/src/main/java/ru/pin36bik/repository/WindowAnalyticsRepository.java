package ru.pin36bik.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin36bik.entity.WindowAnalytics;

public interface WindowAnalyticsRepository
        extends JpaRepository<WindowAnalytics, String> {
    List<WindowAnalytics> findByWindowId(String windowId);

    List<WindowAnalytics> findByActive(boolean isActive);
}
