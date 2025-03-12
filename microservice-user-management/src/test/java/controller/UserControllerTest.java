package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserLoginDTO;
import ru.pin36bik.dto.UserRegistrationDTO;
import ru.pin36bik.entity.User;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.pin36bik.repository.UserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private MockMvc mockMvc;
    private UserRepository userRepository;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
    }

    @Test
    public void testUserRepository() {
        assertNotNull(userRepository);
    }

    @Test
    void testRegisterUser() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setName("John");
        registrationDTO.setBirthday(LocalDate.of(1990, 1, 1));
        registrationDTO.setEmail("john.smith@gmail.com");
        registrationDTO.setPassword("password");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationDTO)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").
                            value("john.smith@gmail.com"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertTrue(userRepository.existsByEmail("john.smith@gmail.com"));
    }

    @Test
    void testAuthorizeUser() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("john.smith@gmail.com");
        loginDTO.setPassword("password");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(loginDTO)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                            .value("john.smith@gmail.com"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                            .value("John"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetUsers() {
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setEmail("email" + i + "@gmail.com");
            userRepository.save(user);
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/user/get"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].email")
                            .value("email0@gmail.com"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].email")
                            .value("email1@gmail.com"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(2, userRepository.count());
    }

    @Test
    @WithMockUser(username = "john.smith@gmail.com")
    void testUpdateCurrentUser() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Max");
        userDTO.setLastName("Smith");

        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Проверяем статус 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Max")) // Проверяем обновлённое имя
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith")); // Проверяем обновлённую фамилию
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testDeleteCurrentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse(userRepository.existsByEmail("test@example.com"));
    }

    @Test
    @WithMockUser(username = "nonexisted@example.com")
    void testDeleteCurrentUserWithUserNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}