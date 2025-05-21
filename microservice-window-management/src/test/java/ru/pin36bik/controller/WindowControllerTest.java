//package ru.pin36bik.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.hamcrest.Matchers.hasSize;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import ru.pin36bik.dto.*;
//import ru.pin36bik.service.WindowService;
//import ru.pin36bik.exceptions.*;
//
//import java.util.List;
//import java.util.function.Function;
//
//import static org.hamcrest.Matchers.*;
//
//@WebMvcTest(WindowController.class)
//@MockBean(WindowService.class)
//class WindowControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private WindowService windowService;
//
//    @MockBean
//    private WebClient webClient;
//
//    private final ObjectMapper objectMapper = new ObjectMapper()
//            .registerModule(new JavaTimeModule());
//
//    // Тестовые данные
//    private final String TEST_EMAIL = "user@example.com";
//    private final String VALID_TOKEN = "valid.token.here";
//
//    private WindowRequest createWindowRequest() {
//        return WindowRequest.builder()
//                .windowId(1L)
//                .name("Kitchen Window")
//                .build();
//    }
//
//    private WindowResponse createWindowResponse() {
//        return WindowResponse.builder()
//                .window_id(1L)
//                .userEmail(TEST_EMAIL)
//                .name("Kitchen Window")
//                .status(true)
//                .presets("presets")
//                .cityName("Zelenograd")
//                .latitude(1.0)
//                .longitude(2.0)
//                .build();
//    }
//
//    private List<WindowUserDTO> createWindowList() {
//        return List.of(
//                WindowUserDTO.builder()
//                        .windowId(1L)
//                        .userEmail(TEST_EMAIL)
//                        .name("Window 1")
//                        .status(true)
//                        .presets("presets")
//                        .location(new WindowLocationDTO("Zelenograd", 1.0, 2.0))
//                        .build(),
//                WindowUserDTO.builder()
//                        .windowId(2L)
//                        .userEmail(TEST_EMAIL + "2")
//                        .name("Window 2")
//                        .status(true)
//                        .presets("presets2")
//                        .location(new WindowLocationDTO("Zelenograd2", 2.0, 3.0))
//                        .build()
//        );
//    }
//
//    @BeforeEach
//    void setUp() {
//        // Мокаем успешную валидацию токена
//        mockWebClientValidation(true);
//    }
//    // ================ ADD WINDOW ===================
//    @Test
//    void addWindow_ShouldReturnCreatedWindow() throws Exception {
//        when(windowService.addWindow(any(WindowRequest.class), eq(TEST_EMAIL)))
//                .thenReturn(createWindowResponse());
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/v1/windows/add")
//                        .header("X-User-Email", TEST_EMAIL)
//                        .header("X-Valid-Token", "true")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createWindowRequest())))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.name").value("Kitchen Window"));
//
//        verify(windowService).addWindow(any(WindowRequest.class), eq(TEST_EMAIL));
//    }
//
//    @Test
//    void addWindow_ShouldRejectInvalidToken() throws Exception {
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/v1/windows/add")
//                        .header("X-User-Email", TEST_EMAIL)
//                        .header("X-Valid-Token", "false")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createWindowRequest())))
//                .andExpect(status().isUnauthorized())
//                .andExpect(result -> assertTrue(
//                        result.getResolvedException() instanceof InvalidTokenException));
//
//        verifyNoInteractions(windowService);
//    }
//
//    // ================ UPDATE WINDOW ===================
//    @Test
//    void updateWindow_ShouldReturnUpdatedWindow() throws Exception {
//        WindowRequestForUpdate updateRequest = new WindowRequestForUpdate();
//        updateRequest.setName("Updated Name");
//        WindowResponse updatedResponse = createWindowResponse();
//
//        when(windowService.updateWindow(any(WindowRequestForUpdate.class),
//                eq(TEST_EMAIL), eq(1L)))
//                .thenReturn(updatedResponse);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.patch("/api/v1/windows/update/1")
//                                .header("X-User-Email", TEST_EMAIL)
//                                .header("X-Valid-Token", "true")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Name"))
//                .andExpect(jsonPath("$.direction").value("South"));
//
//        verify(windowService).updateWindow(any(WindowRequestForUpdate.class),
//                eq(TEST_EMAIL), eq(1L));
//    }
//
//    // ================ GET WINDOWS BY USER ===================
//    @Test
//    void getWindowsByUserEmail_ShouldReturnWindowList() throws Exception {
//        List<WindowUserDTO> windows = createWindowList();
//
//        when(windowService.findByUserEmail(TEST_EMAIL)).thenReturn(windows);
//
//        mockMvc.perform(get("/api/v1/windows/user/{userEmail}", TEST_EMAIL)
//                        .header("X-User-Email", TEST_EMAIL)
//                        .header("X-Valid-Token", "true"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name").value("Window 1"))
//                .andExpect(jsonPath("$[1].name").value("Window 2"));
//
//        verify(windowService).findByUserEmail(TEST_EMAIL);
//    }
//
//    // ================ DELETE WINDOW ===================
//    @Test
//    void deleteWindow_ShouldReturnNoContent() throws Exception {
//        doNothing().when(windowService).deleteWindow(1L, TEST_EMAIL);
//
//        mockMvc.perform(delete("/api/v1/windows/delete/1")
//                        .header("X-User-Email", TEST_EMAIL)
//                        .header("X-Valid-Token", "true"))
//                .andExpect(status().isNoContent());
//
//        verify(windowService).deleteWindow(1L, TEST_EMAIL);
//    }
//
//    @Test
//    void deleteWindow_ShouldRejectIfNotOwner() throws Exception {
//        doThrow(new AccessDeniedException("Not owner"))
//                .when(windowService).deleteWindow(1L, "other@email.com");
//
//        mockMvc.perform(delete("/api/v1/windows/delete/1")
//                        .header("X-User-Email", "other@email.com")
//                        .header("X-Valid-Token", "true"))
//                .andExpect(status().isForbidden());
//
//        verify(windowService).deleteWindow(1L, "other@email.com");
//    }
//
//    // ================ GET WINDOW LOCATION ===================
//    @Test
//    void getWindowLocation_ShouldReturnLocation() throws Exception {
//        WindowLocationDTO locationDTO = new WindowLocationDTO(
//                "Main street 1", 50.4501, 30.5234);
//
//        when(windowService.getLocation(1L, TEST_EMAIL)).thenReturn(locationDTO);
//
//        mockMvc.perform(get("/api/v1/windows/location/1")
//                        .header("X-User-Email", TEST_EMAIL)
//                        .header("X-Valid-Token", "true"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.address", is("Main street 1")))
//                .andExpect(jsonPath("$.latitude", is(50.4501)))
//                .andExpect(jsonPath("$.longitude", is(30.5234)));
//
//        verify(windowService).getLocation(1L, TEST_EMAIL);
//    }
//
//    // ================ COMMON TESTS ===================
//    @Test
//    void allEndpoints_ShouldRequireValidToken() throws Exception {
//        // Проверяем все endpoint'ы на требование валидного токена
//        testEndpointTokenRequirement(
//                MockMvcRequestBuilders.post("/api/v1/windows/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"),
//                "addWindow");
//
//        testEndpointTokenRequirement(
//                MockMvcRequestBuilders.patch("/api/v1/windows/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"),
//                "updateWindow");
//
//        testEndpointTokenRequirement(
//                get("/api/v1/windows/user/test@email.com"),
//                "getWindowsByUserEmail");
//
//        testEndpointTokenRequirement(
//                delete("/api/v1/windows/delete/1"),
//                "deleteWindow");
//
//        testEndpointTokenRequirement(
//                get("/api/v1/windows/location/1"),
//                "getWindowLocation");
//    }
//
//    private void testEndpointTokenRequirement(MockHttpServletRequestBuilder requestBuilder,
//                                              String methodName) throws Exception {
//        mockMvc.perform(requestBuilder
//                        .header("X-User-Email", TEST_EMAIL)
//                        .header("X-Valid-Token", "false"))
//                .andExpect(status().isUnauthorized())
//                .andExpect(result -> {
//                    assertTrue(result.getResolvedException() instanceof InvalidTokenException);
//                    assertEquals("Token validation failed",
//                            result.getResolvedException().getMessage());
//                });
//
//        verify(windowService, never()).getClass().getMethod(methodName, any());
//    }
//
//    private void mockWebClientValidation(boolean valid) {
//        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
//        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
//        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
//
//        when(webClient.get()).thenReturn(uriSpec);
//        when(uriSpec.uri("http://localhost:8082/api/v1/auth/validate")).thenReturn(headersSpec);
//        when(headersSpec.header(eq(HttpHeaders.AUTHORIZATION), anyString())).thenReturn(headersSpec);
//
//        if (valid) {
//            when(headersSpec.exchangeToMono(any())).thenAnswer(inv -> {
//                Function<ClientResponse, Mono<?>> handler = inv.getArgument(0);
//                ClientResponse response = ClientResponse.create(HttpStatus.OK).build();
//                return handler.apply(response);
//            });
//        } else {
//            when(headersSpec.exchangeToMono(any())).thenAnswer(inv -> {
//                Function<ClientResponse, Mono<?>> handler = inv.getArgument(0);
//                ClientResponse response = ClientResponse.create(HttpStatus.UNAUTHORIZED).build();
//                return handler.apply(response);
//            });
//        }
//    }
//
////    private void verifyWebClientCalled() {
////        verify(webClient).get();
////        verify(webClient).uri("http://localhost:8082/api/v1/auth/validate");
////        verify(webClient).header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN);
////    }
//}