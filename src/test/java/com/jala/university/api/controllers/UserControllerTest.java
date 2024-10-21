package com.jala.university.api.controllers;

import com.jala.university.core.services.UserService;
import com.jala.university.core.utils.UserValidator;
import com.jala.university.data.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

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
        doNothing().when(userValidator).validateGet(anyString());
        doNothing().when(userValidator).validateUserExistsLogin(anyString());
        doNothing().when(userValidator).validateDelete(anyString());
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test-456");
        userDTO.setName("Good Graces");
        userDTO.setLogin("Taste");
        userDTO.setPassword("please456");

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
    void testGetUsers() throws Exception {
        when(userService.getUsers()).thenReturn(Arrays.asList(userDTO));

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"test-123\",\"name\":\"Baby Shark\",\"login\":\"Shark\",\"password\":\"pass123\"}]"));
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
        when(userService.getByLogin("UnknownLogin")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/login/UnknownLogin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("root");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("test-123");
        updatedUserDTO.setName("Updated Shark");
        updatedUserDTO.setLogin("Shark");
        updatedUserDTO.setPassword("newPass123");

        when(userService.updateUser(eq("test-123"), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));
        when(userService.getById("test-123")).thenReturn(Optional.of(updatedUserDTO));

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
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("root");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("unknownId");
        updatedUserDTO.setName("Unknown User");

        when(userService.updateUser(any(String.class), any(UserDTO.class))).thenReturn(Optional.empty());
        when(userService.getById("unknownId")).thenReturn(Optional.empty());

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put("/api/users/unknownId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserWithNewPassword() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("root");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("test-123");
        updatedUserDTO.setName("Updated Shark");
        updatedUserDTO.setLogin("Shark");
        updatedUserDTO.setPassword("newSecurePass");

        when(userService.updateUser(eq("test-123"), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));
        when(userService.getById("test-123")).thenReturn(Optional.of(updatedUserDTO));

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
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("root");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("test-123");
        updatedUserDTO.setName("Updated Shark");
        updatedUserDTO.setLogin("Shark");
        updatedUserDTO.setPassword(null);

        when(userService.updateUser(eq("test-123"), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));
        when(userService.getById("test-123")).thenReturn(Optional.of(updatedUserDTO));

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
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("root");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId("test-123");
        updatedUserDTO.setName("Baby Shark");
        updatedUserDTO.setLogin("newLogin");
        updatedUserDTO.setPassword("pass123");

        when(userService.updateUser(eq("test-123"), any(UserDTO.class))).thenReturn(Optional.of(updatedUserDTO));
        when(userService.getById("test-123")).thenReturn(Optional.of(updatedUserDTO));

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put("/api/users/test-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"test-123\",\"name\":\"Baby Shark\",\"login\":\"newLogin\",\"password\":\"pass123\"}"));
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test-456");
        userDTO.setName("New User");
        userDTO.setLogin("newuser");
        userDTO.setPassword("password123");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User newuser registered successfully with ID: test-456")));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("root");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.getById("test-123")).thenReturn(Optional.of(userDTO));
        when(userService.deleteUser("test-123")).thenReturn(true);

        mockMvc.perform(delete("/api/users/test-123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User deleted successfully")));
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("root");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.getById("unknownId")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/users/unknownId"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserForbidden() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("otherUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.getById("test-123")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(delete("/api/users/test-123"))
                .andExpect(status().isForbidden());
    }
}