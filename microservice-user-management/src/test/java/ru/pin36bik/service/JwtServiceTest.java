package ru.pin36bik.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pin36bik.config.JwtConfig;
import ru.pin36bik.dto.LoginResponse;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.entity.User;
import ru.pin36bik.entity.role.Role;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenFactory;
import ru.pin36bik.utils.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenFactory jwtTokenFactory;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private JwtService jwtService;

    @Test
    void generateTokenPair_ForAdminUser_ShouldReturnTokensWithAdminRole() {
        // Arrange
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("admin@test.com");
        adminUser.setRole(Role.ADMIN);

        UserDTO userDTO = new UserDTO();
        when(userMapper.toDTO(adminUser)).thenReturn(userDTO);
        when(jwtTokenFactory.createAccessToken(adminUser, List.of("ADMIN")))
                .thenReturn("admin-access-token");
        when(jwtTokenFactory.createRefreshToken(adminUser))
                .thenReturn("admin-refresh-token");
        when(jwtConfig.getRefreshTtl()).thenReturn(86400000L); // 24 часа

        // Act
        LoginResponse response = jwtService.generateTokenPair(adminUser);

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("admin-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("admin-refresh-token");
        assertThat(response.getUserDTO()).isEqualTo(userDTO);

        verify(userRepository).save(adminUser);
        assertThat(adminUser.getRefreshToken()).isEqualTo("admin-refresh-token");
        assertThat(adminUser.getRefreshTokenExpiry())
                .isAfter(LocalDateTime.now().plusHours(23));
    }

    @Test
    void generateTokenPair_ForRegularUser_ShouldReturnTokensWithUserRole() {
        // Arrange
        User regularUser = new User();
        regularUser.setId(2L);
        regularUser.setEmail("user@test.com");
        regularUser.setRole(Role.USER);

        UserDTO userDTO = new UserDTO();
        when(userMapper.toDTO(regularUser)).thenReturn(userDTO);
        when(jwtTokenFactory.createAccessToken(regularUser, List.of("USER")))
                .thenReturn("user-access-token");
        when(jwtTokenFactory.createRefreshToken(regularUser))
                .thenReturn("user-refresh-token");
        when(jwtConfig.getRefreshTtl()).thenReturn(86400000L);

        // Act
        LoginResponse response = jwtService.generateTokenPair(regularUser);

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("user-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("user-refresh-token");
        assertThat(response.getUserDTO()).isEqualTo(userDTO);

        verify(userRepository).save(regularUser);
        assertThat(regularUser.getRefreshToken()).isEqualTo("user-refresh-token");
    }

    @Test
    void generateTokenPair_ShouldSetCorrectRefreshTokenExpiry() {
        // Arrange
        User user = new User();
        user.setId(3L);
        user.setRole(Role.USER);

        when(jwtTokenFactory.createAccessToken(any(), any())).thenReturn("token");
        when(jwtTokenFactory.createRefreshToken(any())).thenReturn("refresh-token");

        // Тестируем с разными TTL
        when(jwtConfig.getRefreshTtl()).thenReturn(3600000L); // 1 час

        // Act
        jwtService.generateTokenPair(user);

        // Assert
        assertThat(user.getRefreshTokenExpiry())
                .isBetween(
                        LocalDateTime.now().plusMinutes(59),
                        LocalDateTime.now().plusMinutes(61)
                );
    }

    @Test
    void generateTokenPair_ShouldSaveUserWithUpdatedTokens() {
        // Arrange
        User user = new User();
        user.setId(4L);
        user.setRole(Role.USER);

        when(jwtTokenFactory.createAccessToken(any(), any())).thenReturn("token");
        when(jwtTokenFactory.createRefreshToken(any())).thenReturn("refresh-token");
        when(jwtConfig.getRefreshTtl()).thenReturn(10000L);

        // Act
        jwtService.generateTokenPair(user);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(savedUser.getRefreshTokenExpiry())
                .isAfter(LocalDateTime.now());
    }

    @Test
    void generateTokenPair_ShouldReturnCorrectResponseStructure() {
        // Arrange
        User user = new User();
        user.setId(5L);
        user.setRole(Role.USER);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(5L);

        when(userMapper.toDTO(user)).thenReturn(userDTO);
        when(jwtTokenFactory.createAccessToken(any(), any())).thenReturn("access");
        when(jwtTokenFactory.createRefreshToken(any())).thenReturn("refresh");
        when(jwtConfig.getRefreshTtl()).thenReturn(1000L);

        // Act
        LoginResponse response = jwtService.generateTokenPair(user);

        // Assert
        assertThat(response)
                .hasFieldOrPropertyWithValue("accessToken", "access")
                .hasFieldOrPropertyWithValue("refreshToken", "refresh")
                .hasFieldOrPropertyWithValue("userDTO", userDTO);
    }
}