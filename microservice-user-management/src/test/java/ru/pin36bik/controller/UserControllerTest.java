package ru.pin36bik.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserDTOForAdmin;
import ru.pin36bik.entity.User;
import ru.pin36bik.entity.role.Role;
import ru.pin36bik.handler.GlobalExceptionHandler;
import ru.pin36bik.security.filter.JwtAuthFilter;
import ru.pin36bik.service.CookieService;
import ru.pin36bik.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private CookieService cookieService;

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getAllUsersByAdmin_ShouldReturnUsersList() throws Exception {
        List<UserDTOForAdmin> users = List.of(
                new UserDTOForAdmin(1L, "John", "Doe", LocalDate.now(), "john@test.com", LocalDateTime.now(), Role.USER)
        );

        when(userService.getAllUsersDTOs()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users/admin/get_users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

//    @Test
//    @WithMockCustomUser
//    void getUserById_ShouldReturnUser() throws Exception {
//        // Arrange
//        User user = new User();
//        user.setId(1L);
//        user.setName("John");
//        user.setEmail("test@example.com");
//
//        when(userService.getUserById(1L)).thenReturn(user);
//
//        // Мокируем поведение фильтра
//        doAnswer(invocation -> {
//            HttpServletRequest request = invocation.getArgument(0);
//            HttpServletResponse response = invocation.getArgument(1);
//            FilterChain filterChain = invocation.getArgument(2);
//
//            // Устанавливаем аутентификацию
//            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                    user, // передаем реального пользователя
//                    null,
//                    user.getAuthorities()
//            );
//            SecurityContextHolder.getContext().setAuthentication(auth);
//
//            filterChain.doFilter(request, response);
//            return null; // для void методов
//        }).when(jwtAuthFilter).doFilterInternal(any(), any(), any());
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/users/admin/get_by_id/1")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer mocktoken"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("John"));
//    }
//
//    @Test
//    @WithMockUser(username = "user@test.com")
//    void updateCurrentUser_ShouldUpdateUser() throws Exception {
//        // Arrange
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("Updated");
//
//        when(userService.updateCurrentUser(eq("user@test.com"), any(UserDTO.class)))
//                .thenReturn(userDTO);
//
//        // Act & Assert
//        mockMvc.perform(put("/api/v1/users/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Updated\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated"));
//    }

    @Test
    @WithMockUser(username = "user@test.com")
    void deleteAndArchiveCurrentUser_ShouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteAndArchiveUser("user@test.com");
        doNothing().when(cookieService).expireAllCookies(any());

        mockMvc.perform(delete("/api/v1/users/delete?email=user@test.com"))
                .andExpect(status().isNoContent());
    }
}