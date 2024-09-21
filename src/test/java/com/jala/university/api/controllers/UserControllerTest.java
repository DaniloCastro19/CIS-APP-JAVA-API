package com.jala.university.api.controllers;

import com.jala.university.core.services.UserService;
import com.jala.university.core.utils.UserValidator;
import com.jala.university.data.dto.UserDTO;
import com.jala.university.data.models.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setValidator(new LocalValidatorFactoryBean())
                .build();

        userDTO = new UserDTO();
        userDTO.setId("test-123");
        userDTO.setName("Baby Shark");
        userDTO.setLogin("Shark");
        userDTO.setPassword("pass123");

        doNothing().when(userValidator).validate(any(UserDTO.class));
        doNothing().when(userValidator).validateUpdate(anyString(), any(UserDTO.class));
    }

    // User Post
    @Test
    void testCreateUserSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test-456");
        userDTO.setName("Good Graces");
        userDTO.setLogin("Taste");
        userDTO.setPassword("please456");

        UserModel userModel = new UserModel();
        userModel.setId("test-456");
        userModel.setName("Good Graces");
        userModel.setLogin("Taste");
        userModel.setPassword("please456");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(userJson));
    }

    @Test
    void testCreateUserWithMinimumRequiredFields() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test-456");
        userDTO.setName("Basic User");
        userDTO.setLogin("basicuser");
        userDTO.setPassword("Hi123456");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(userJson));
    }

    @Test
    void testCreateUserWithComplexPassword() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test-789 ");
        userDTO.setName("Complex Pass User");
        userDTO.setLogin("complexUser");
        userDTO.setPassword("Comp!ex@2024");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(userJson));
    }

    // User Get
    @Test
    void testGetUsers() throws Exception {
        when(userService.getUsers()).thenReturn(Arrays.asList(userDTO));

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"test-123\",\"name\":\"Baby Shark\",\"login\":\"Shark\",\"password\":\"pass123\"}]"));
    }

    @Test
    void testGetMultipleUsers() throws Exception {
        UserDTO anotherUserDTO = new UserDTO();
        anotherUserDTO.setId("test-24");
        anotherUserDTO.setName("Bed Chem");
        anotherUserDTO.setLogin("Apple");
        anotherUserDTO.setPassword("apple123");

        when(userService.getUsers()).thenReturn(Arrays.asList(userDTO, anotherUserDTO));

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"test-123\",\"name\":\"Baby Shark\",\"login\":\"Shark\",\"password\":\"pass123\"}, {\"id\":\"test-24\",\"name\":\"Bed Chem\",\"login\":\"Apple\",\"password\":\"apple123\"}]"));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getById("test-123")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/test-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"test-123\",\"name\":\"Baby Shark\",\"login\":\"Shark\",\"password\":\"pass123\"}"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(userService.getById("UnknownId")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/UnknownId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserByLogin() throws Exception {
        when(userService.getByLogin("Shark")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/login/Shark")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"test-123\",\"name\":\"Baby Shark\",\"login\":\"Shark\",\"password\":\"pass123\"}"));
    }

    @Test
    void testGetUserByLoginNotFound() throws Exception {
        when(userService.getById("UnknownLogin")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/login/UnknownLogin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // User Update
    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("test-123");
        updatedUserDTO.setName("Updated Shark");
        updatedUserDTO.setLogin("Shark");
        updatedUserDTO.setPassword("newPass123");

        when(userService.updateUser(any(String.class), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put("/api/users/test-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userJson));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("unknownId");
        updatedUserDTO.setName("Unknown User");

        when(userService.updateUser(any(String.class), any(UserDTO.class))).thenReturn(Optional.empty());

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put("/api/users/unknownId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserWithNewPassword() throws Exception {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("test-123");
        updatedUserDTO.setName("Updated Shark");
        updatedUserDTO.setLogin("Shark");
        updatedUserDTO.setPassword("newSecurePass");

        when(userService.updateUser(any(String.class), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put("/api/users/test-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userJson));
    }

    @Test
    void testUpdateUserWithoutPasswordChange() throws Exception {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("test-123");
        updatedUserDTO.setName("Updated Shark");
        updatedUserDTO.setLogin("Shark");
        updatedUserDTO.setPassword(null);

        when(userService.updateUser(any(String.class), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put("/api/users/test-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userJson));
    }

    @Test
    void testUpdateUserOnlyLogin() throws Exception {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("test-123");
        updatedUserDTO.setName("Baby Shark");
        updatedUserDTO.setLogin("newLogin");
        updatedUserDTO.setPassword("pass123");  

        when(userService.updateUser(any(String.class), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put("/api/users/test-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"test-123\",\"name\":\"Baby Shark\",\"login\":\"newLogin\",\"password\":\"pass123\"}"));
    }

    // User Delete
    @Test
    void testDeleteUserSuccess() throws Exception {
        String userId = "test-123";
        when(userService.deleteUser(userId)).thenReturn(true);

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"User deleted successfully\"}"));
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        String userId = "unknown-id";
        when(userService.deleteUser(userId)).thenReturn(false);

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"User not found\"}"));
    }

}