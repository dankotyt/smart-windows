package ru.pin36bik.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserDTOForAdmin;
import ru.pin36bik.entity.User;
import ru.pin36bik.entity.role.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Иван");
        user.setSurname("Иванов");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        user.setEmail("ivan@example.com");

        // Act
        UserDTO result = userMapper.toDTO(user);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Иван");
        assertThat(result.getSurname()).isEqualTo("Иванов");
        assertThat(result.getBirthday()).isEqualTo(LocalDate.of(1990, 5, 15));
        assertThat(result.getEmail()).isEqualTo("ivan@example.com");
    }

    @Test
    void toDTO_ShouldHandleNullValuesGracefully() {
        // Arrange
        User user = new User();
        user.setId(1L);
        // Остальные поля null

        // Act
        UserDTO result = userMapper.toDTO(user);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isNull();
        assertThat(result.getSurname()).isNull();
        assertThat(result.getBirthday()).isNull();
        assertThat(result.getEmail()).isNull();
    }

    @Test
    void toDTOForAdmin_ShouldMapAllFieldsIncludingAdminSpecific() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Алексей");
        user.setSurname("Петров");
        user.setBirthday(LocalDate.of(1985, 8, 20));
        user.setEmail("alex@example.com");
        user.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        user.setRole(Role.ADMIN);

        // Act
        UserDTOForAdmin result = userMapper.toDTOForAdmin(user);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Алексей");
        assertThat(result.getSurname()).isEqualTo("Петров");
        assertThat(result.getBirthday()).isEqualTo(LocalDate.of(1985, 8, 20));
        assertThat(result.getEmail()).isEqualTo("alex@example.com");
        assertThat(result.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void toDTOForAdmin_ShouldHandleNullValuesForAdminFields() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Сергей");
        // created_at и role не установлены (null)

        // Act
        UserDTOForAdmin result = userMapper.toDTOForAdmin(user);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Сергей");
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getRole()).isNull();
    }

    @Test
    void toDTO_ShouldNotContainAdminFields() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.USER);

        // Act
        UserDTO result = userMapper.toDTO(user);

        // Assert
        // Проверяем, что в обычном DTO нет полей для админа
        assertThat(result).isExactlyInstanceOf(UserDTO.class);
        assertThat(result).isNotInstanceOf(UserDTOForAdmin.class);
    }
}