//package ru.pin36bik.config;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest
//@Import(SecurityConfig.class)
//class SecurityConfigTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void whenValidToken_thenAccessGranted() throws Exception {
//        mockMvc.perform(get("/api/v1/windows/test")
//                        .header("X-Valid-Token", "true"))
//                .andExpect(status().isNotFound()); // 404 потому что endpoint не существует, но доступ разрешен
//    }
//
//    @Test
//    void whenInvalidToken_thenAccessDenied() throws Exception {
//        mockMvc.perform(get("/api/v1/windows/test")
//                        .header("X-Valid-Token", "false"))
//                .andExpect(status().isUnauthorized());
//    }
//}