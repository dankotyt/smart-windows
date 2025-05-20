package ru.pin36bik.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import ru.pin36bik.MicroserviceUsersApplication;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.LoginRequest;
import ru.pin36bik.dto.RegisterRequest;
import ru.pin36bik.entity.ArchivedUser;
import ru.pin36bik.entity.User;
import ru.pin36bik.repository.ArchivedUserRepository;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.service.UserService;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MicroserviceUsersApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArchivedUserRepository archivedUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper;

    private Long userId;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        archivedUserRepository.deleteAll();

        User user = new User();
        user.setName("Test");
        user.setSurname("User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));

        userRepository.save(user);

        userId = user.getId();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testUserRepository() {
        assertNotNull(userRepository);
    }

    @Test
    void testRegisterUser() throws Exception {

        RegisterRequest registrationDTO = new RegisterRequest();
        registrationDTO.setName("John");
        registrationDTO.setSurname("Smith");
        registrationDTO.setBirthday(LocalDate.of(1990, 1, 1));
        registrationDTO.setEmail("john.smith@gmail.com");
        registrationDTO.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .with(csrf().asHeader()) // Добавляем CSRF-токен в заголовок
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.smith@gmail.com"));

        Optional<User> savedUser = userRepository.findByEmail("john.smith@gmail.com");
        assertTrue(savedUser.isPresent());
        assertEquals("John", savedUser.get().getName());
    }

    @Test
    void testRegisterUserWithExistingEmail() throws Exception {
        // Создаем пользователя с email "john.smith@gmail.com"
        User existingUser = new User();
        existingUser.setName("Existing");
        existingUser.setSurname("User");
        existingUser.setBirthday(LocalDate.of(1990, 1, 1));
        existingUser.setEmail("john.smith@gmail.com");
        existingUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(existingUser);

        // Пытаемся зарегистрировать нового пользователя с тем же email
        RegisterRequest registrationDTO = new RegisterRequest();
        registrationDTO.setName("John");
        registrationDTO.setSurname("Smith");
        registrationDTO.setBirthday(LocalDate.of(1990, 1, 1));
        registrationDTO.setEmail("john.smith@gmail.com");
        registrationDTO.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .with(csrf().asHeader()) // Добавляем CSRF-токен в заголовок
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Ожидаем статус 400
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The user has already existed with this email!"));
    }

    @Test
    @WithMockUser
    void testAuthorizeUser() throws Exception {
        LoginRequest loginDTO = new LoginRequest();
        loginDTO.setEmail("test@gmail.com");
        loginDTO.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .with(csrf()) // Добавляем CSRF-токен
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                        .value("test@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("Test"));
    }

    @Test
    void testAuthorizeUserWithoutCsrf() throws Exception {
        LoginRequest loginDTO = new LoginRequest();
        loginDTO.setEmail("test@gmail.com");
        loginDTO.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden()); // Ожидаем 403
    }

    @Test
    void testGetUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/get_users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email")
                        .value("test@gmail.com"));

        assertEquals(1, userRepository.count());
    }

    @Test
    void testGetUserByEmail() throws Exception {
        User user = userRepository.findByEmail("test@gmail.com")
                .orElseThrow(() -> new RuntimeException("User not found!"));

        String email = user.getEmail();
        mockMvc.perform(MockMvcRequestBuilders.get("/user/get_by_email/{email}",
                                email) // Используем {email} для PathVariable
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("User"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@gmail.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/user/get_by_id/{id}",
                                userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("User"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@gmail.com"));
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void testUpdateCurrentUser() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Max");
        userDTO.setSurname("Smith");

        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                        .with(csrf()) // Добавляем CSRF-токен
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Проверяем статус 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Max")) // Проверяем обновлённое имя
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith")); // Проверяем обновлённую фамилию
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void testDeleteCurrentUser() throws Exception {
        User user = userRepository.findByEmail("test@gmail.com")
                .orElseThrow(() -> new RuntimeException("User not found!"));

        userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/{id}",
                                userId.toString()) // Используем {id} для PathVariable
                        .with(csrf())) // Добавляем CSRF-токен
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertFalse(userRepository.existsByEmail("test@gmail.com"));
    }

    @Test
    void testDeleteAndArchiveUser() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        userService.deleteAndArchiveUser("john.doe@example.com");

        Optional<User> deletedUser = userRepository.findByEmail("john.doe@example.com");
        assertFalse(deletedUser.isPresent());

        Optional<ArchivedUser> archivedUser = archivedUserRepository.findByEmail("john.doe@example.com");
        assertTrue(archivedUser.isPresent());
        assertEquals("John", archivedUser.get().getName());
        assertEquals("Doe", archivedUser.get().getSurname());
    }
}


