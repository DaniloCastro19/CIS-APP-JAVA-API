package com.jala.university.api.controllers;

import com.jala.university.core.services.UserService;
import com.jala.university.data.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test-id");
        userDTO.setName("Test User");
        userDTO.setLogin("testlogin");
        userDTO.setPassword("password123");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userJson));
    }

    @Test
    void testCreateUserInvalidData() throws Exception {
        UserDTO invalidUserDTO = new UserDTO();
        invalidUserDTO.setId("test-id");
        invalidUserDTO.setName("");

        ObjectMapper objectMapper = new ObjectMapper();
        String invalidUserJson = objectMapper.writeValueAsString(invalidUserDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUserUnexpectedResult() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Test User");
        userDTO.setLogin("testlogin");
        userDTO.setPassword("password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateUserInvalidNameLength() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test-id");
        userDTO.setName("Test User With Invalid Name Length Exceeding Maximum Characters");
        userDTO.setLogin("testlogin");
        userDTO.setPassword("password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUserNullPassword() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test-id");
        userDTO.setName("Test User");
        userDTO.setLogin("testlogin");
        userDTO.setPassword(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }
}