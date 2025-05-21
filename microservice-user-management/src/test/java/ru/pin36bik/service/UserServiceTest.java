package ru.pin36bik.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserDTOForAdmin;
import ru.pin36bik.entity.ArchivedUser;
import ru.pin36bik.entity.User;
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.repository.ArchivedUserRepository;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.utils.UserMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArchivedUserRepository archivedUserRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsersDTOs_ShouldReturnList() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTOForAdmin(user)).thenReturn(new UserDTOForAdmin());

        List<UserDTOForAdmin> result = userService.getAllUsersDTOs();

        assertEquals(1, result.size());
    }

    @Test
    void getUserByEmail_ShouldReturnUser() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("test@test.com");

        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void getUserByEmail_ShouldThrowWhenNotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail("test@test.com");
        });
    }

    @Test
    void updateCurrentUser_ShouldUpdateFields() {
        User user = new User();
        user.setEmail("old@test.com");
        user.setRefreshToken("token");

        UserDTO userDTO = new UserDTO();
        userDTO.setName("New Name");
        userDTO.setEmail("new@test.com");

        when(userRepository.findByEmail("old@test.com")).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(userMapper.toDTO(any())).thenReturn(userDTO);

        UserDTO result = userService.updateCurrentUser("old@test.com", userDTO);

        assertEquals("New Name", result.getName());
        assertEquals("new@test.com", result.getEmail());
    }

    @Test
    void deleteAndArchiveUser_ShouldArchiveAndDelete() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        userService.deleteAndArchiveUser("test@test.com");

        verify(archivedUserRepository).save(any(ArchivedUser.class));
        verify(userRepository).delete(user);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("test@test.com");

        assertEquals("test@test.com", userDetails.getUsername());
    }
}