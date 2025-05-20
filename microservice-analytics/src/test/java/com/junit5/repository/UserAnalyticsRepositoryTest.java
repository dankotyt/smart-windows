package com.junit5.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import ru.pin36bik.AnalyticsApplication;
import ru.pin36bik.entity.UserAnalytics;
import ru.pin36bik.repository.UserAnalyticsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = AnalyticsApplication.class)
class UserAnalyticsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserAnalyticsRepository repository;

    @Test
    void shouldFindByUserId() {
        LocalDateTime now = LocalDateTime.now();
        UserAnalytics user1 = new UserAnalytics();
        user1.setLoginCount(5);
        user1.setLastLogin(now.minusDays(1));
        user1.setTimestamp(now);
        entityManager.persist(user1);

        UserAnalytics user2 = new UserAnalytics();
        user2.setLoginCount(3);
        user2.setLastLogin(now.minusDays(2));
        user2.setTimestamp(now);
        entityManager.persist(user2);

        entityManager.flush();

        List<UserAnalytics> result = repository.findByUserId(user1.getUserId());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUserId()).isEqualTo(user1.getUserId());
        assertThat(result.getFirst().getLoginCount()).isEqualTo(5);
        assertThat(result.getFirst().getLastLogin()).isEqualTo(now.minusDays(1));
        assertThat(result.getFirst().getTimestamp()).isEqualTo(now);
    }

    @Test
    void shouldReturnEmptyListWhenUserIdNotFound() {
        LocalDateTime now = LocalDateTime.now();
        UserAnalytics user = new UserAnalytics();
        user.setLoginCount(5);
        user.setLastLogin(now.minusDays(1));
        user.setTimestamp(now);
        entityManager.persist(user);
        entityManager.flush();

        List<UserAnalytics> result = repository.findByUserId(user.getUserId() - 999L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindOldestLoggedInUser() {
        LocalDateTime now = LocalDateTime.now();
        UserAnalytics user1 = new UserAnalytics();
        user1.setLoginCount(5);
        user1.setLastLogin(now.minusDays(1));
        user1.setTimestamp(now);
        entityManager.persist(user1);

        UserAnalytics user2 = new UserAnalytics();
        user2.setLoginCount(3);
        user2.setLastLogin(now.minusDays(2));
        user2.setTimestamp(now);
        entityManager.persist(user2);

        entityManager.flush();

        Optional<UserAnalytics> result = repository.findOldestLoggedInUser();

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(user2.getUserId());
        assertThat(result.get().getLastLogin()).isEqualTo(now.minusDays(2));
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoUsersExist() {
        Optional<UserAnalytics> result = repository.findOldestLoggedInUser();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldIncrementLoginCountAndUpdateLastLogin() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newLoginTime = now.plusHours(1).truncatedTo(java.time.temporal.ChronoUnit.MILLIS);
        UserAnalytics user = new UserAnalytics();
        user.setLoginCount(5);
        user.setLastLogin(now.minusDays(1));
        user.setTimestamp(now);
        entityManager.persist(user);
        entityManager.flush();

        repository.incrementLoginCount(user.getUserId(), newLoginTime);
        entityManager.flush();
        entityManager.clear();

        UserAnalytics updatedUser = entityManager.find(UserAnalytics.class, user.getUserId());
        assertThat(updatedUser.getLoginCount()).isEqualTo(6);
        assertThat(updatedUser.getLastLogin()).isEqualTo(newLoginTime);
    }

    @Test
    void shouldNotIncrementLoginCountWhenUserNotFound() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newLoginTime = now.plusHours(1).truncatedTo(java.time.temporal.ChronoUnit.MILLIS);
        UserAnalytics user = new UserAnalytics();
        user.setLoginCount(5);
        user.setLastLogin(now.minusDays(1).truncatedTo(java.time.temporal.ChronoUnit.MILLIS));
        user.setTimestamp(now);
        entityManager.persist(user);
        entityManager.flush();

        repository.incrementLoginCount(user.getUserId() - 999L, newLoginTime);
        entityManager.flush();
        entityManager.clear();

        UserAnalytics unchangedUser = entityManager.find(UserAnalytics.class, user.getUserId());
        assertThat(unchangedUser.getLoginCount()).isEqualTo(5);
        assertThat(unchangedUser.getLastLogin()).isEqualTo(now.minusDays(1).truncatedTo(java.time.temporal.ChronoUnit.MILLIS));
    }

    @Test
    void shouldSaveAndFindById() {
        LocalDateTime now = LocalDateTime.now();
        UserAnalytics user = new UserAnalytics();
        user.setLoginCount(5);
        user.setLastLogin(now.minusDays(1));
        user.setTimestamp(now);
        UserAnalytics savedUser = repository.save(user);

        Optional<UserAnalytics> foundUser = repository.findById(savedUser.getUserId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(foundUser.get().getLoginCount()).isEqualTo(5);
        assertThat(foundUser.get().getLastLogin()).isEqualTo(now.minusDays(1));
        assertThat(foundUser.get().getTimestamp()).isEqualTo(now);
    }

    @Test
    void shouldDeleteById() {
        LocalDateTime now = LocalDateTime.now();
        UserAnalytics user = new UserAnalytics();
        user.setLoginCount(5);
        user.setLastLogin(now.minusDays(1));
        user.setTimestamp(now);
        UserAnalytics savedUser = repository.save(user);

        repository.deleteById(savedUser.getUserId());
        Optional<UserAnalytics> foundUser = repository.findById(savedUser.getUserId());

        assertThat(foundUser).isEmpty();
    }
}
