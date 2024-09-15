package com.jala.university.api.service;

import com.jala.university.core.services.UserService;
import com.jala.university.core.utils.UserMapper;
import com.jala.university.data.dto.UserDTO;
import com.jala.university.data.models.UserModel;
import com.jala.university.data.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserModel userModel;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userModel = new UserModel();
        userModel.setId("prueba-123");
        userModel.setName("Baby Shark");
        userModel.setLogin("Shark");
        userModel.setPassword("pass123");

        userDTO = new UserDTO();
        userDTO.setId("prueba-123");
        userDTO.setName("Baby Shark");
        userDTO.setLogin("Shark");
        userDTO.setPassword("pass123");
    }

    // User Post
    @Test
    void testCreateUserSuccess() {
        when(userMapper.toModel(any(UserDTO.class))).thenReturn(userModel);
        when(userRepository.createUser(any(UserModel.class))).thenReturn(userModel);
        when(userMapper.toDTO(any(UserModel.class))).thenReturn(userDTO);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("Baby Shark", createdUser.getName());
        assertEquals("Shark", createdUser.getLogin());
        assertEquals("pass123", createdUser.getPassword());
        verify(userRepository, times(1)).createUser(any(UserModel.class));
    }

    @Test
    void testCreateUserInvalidData() {
        UserDTO invalidUser = new UserDTO();
        invalidUser.setName(null);
        invalidUser.setLogin("Shark");
        invalidUser.setPassword("pass123");

        when(userMapper.toModel(any(UserDTO.class))).thenReturn(userModel);
        doThrow(new IllegalArgumentException("The name cannot be null or empty"))
                .when(userRepository).createUser(any(UserModel.class));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(invalidUser));

        verify(userRepository, times(1)).createUser(any(UserModel.class));
    }

    @Test
    void testCreateUserDuplicateLogin() {
        UserDTO newUser = new UserDTO();
        newUser.setName("Baby Shark");
        newUser.setLogin("Shark");
        newUser.setPassword("pass123");

        when(userMapper.toModel(any(UserDTO.class))).thenReturn(userModel);
        doThrow(new IllegalArgumentException("Login already exists"))
                .when(userRepository).createUser(any(UserModel.class));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(newUser));

        verify(userRepository, times(1)).createUser(any(UserModel.class));
    }

    @Test
    void testCreateUserUnexpectedException() {
        UserDTO newUser = new UserDTO();
        newUser.setName("Baby Shark");
        newUser.setLogin("Shark");
        newUser.setPassword("pass123");

        when(userMapper.toModel(any(UserDTO.class))).thenReturn(userModel);
        doThrow(new RuntimeException("Unexpected database error"))
                .when(userRepository).createUser(any(UserModel.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(newUser));

        assertEquals("Unexpected database error", exception.getMessage());
        verify(userRepository, times(1)).createUser(any(UserModel.class));
    }

    // User Get
    @Test
    void testGetUsers() {
        UserModel userModel2 = new UserModel();
        userModel2.setId("test-456");
        userModel2.setName("Good Graces");
        userModel2.setLogin("Taste");
        userModel2.setPassword("pass456");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId("test-456");
        userDTO2.setName("Good Graces");
        userDTO2.setLogin("Taste");
        userDTO2.setPassword("please456");

        when(userRepository.getUsers()).thenReturn(Arrays.asList(userModel, userModel2));
        when(userMapper.toDTO(userModel)).thenReturn(userDTO);
        when(userMapper.toDTO(userModel2)).thenReturn(userDTO2);

        List<UserDTO> users = userService.getUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Baby Shark", users.get(0).getName());
        assertEquals("Good Graces", users.get(1).getName());
        verify(userRepository, times(1)).getUsers();
    }

    @Test
    void testGetById() {
        when(userRepository.getUserById("test-123")).thenReturn(Optional.of(userModel));
        when(userMapper.toDTO(userModel)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getById("test-123");

        assertTrue(result.isPresent());
        assertEquals("Baby Shark", result.get().getName());
        verify(userRepository, times(1)).getUserById("test-123");
    }

    @Test
    void testGetByIdNotFound() {
        when(userRepository.getUserById("UnknownId")).thenReturn(Optional.of(userModel));

        Optional<UserDTO> result = userService.getById("UnknownId");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).getUserById("UnknownId");
    }

    @Test
    void testGetByLogin() {
        when(userRepository.getUserByLogin("Shark")).thenReturn(Optional.of(userModel));
        when(userMapper.toDTO(userModel)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getByLogin("Shark");

        assertTrue(result.isPresent());
        assertEquals("Baby Shark", result.get().getName());
        assertEquals("Shark", result.get().getLogin());
        verify(userRepository, times(1)).getUserByLogin("Shark");
    }

    @Test
    void testGetByLoginNotFound() {
        when(userRepository.getUserByLogin("UnknownLogin")).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getByLogin("UnknownLogin");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).getUserByLogin("UnknownLogin");
    }

    // User Update
    @Test
    void testUpdateUserSuccess() {
        userDTO.setName("Updated Shark");
        userDTO.setLogin("UpdatedLogin");
        userDTO.setPassword("newpass123");

        UserModel updatedUserModel = userModel;
        updatedUserModel.setName("Updated Shark");
        updatedUserModel.setLogin("UpdatedLogin");
        updatedUserModel.setPassword("newpass123");

        when(userRepository.getUserById("test-123")).thenReturn(Optional.of(userModel));
        when(userMapper.toModel(userDTO)).thenReturn(updatedUserModel);
        when(userRepository.updateUserByID(any(UserModel.class))).thenReturn(updatedUserModel);
        when(userMapper.toDTO(any(UserModel.class))).thenReturn(userDTO);

        Optional<UserDTO> result = userService.updateUser("test-123", userDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Shark", result.get().getName());
        assertEquals("UpdatedLogin", result.get().getLogin());
        verify(userRepository, times(1)).getUserById("test-123");
        verify(userRepository, times(1)).updateUserByID(any(UserModel.class));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.getUserById("unknownId")).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.updateUser("unknownId", userDTO);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).getUserById("unknownId");
        verify(userRepository, never()).updateUserByID(any(UserModel.class));
    }

    @Test
    void testUpdateUserPartialData() {
        userDTO.setName("Chihiro");

        UserModel updatedUserModel = userModel;
        updatedUserModel.setName("Chihiro");

        when(userRepository.getUserById("test-123")).thenReturn(Optional.of(userModel));
        when(userMapper.toModel(userDTO)).thenReturn(updatedUserModel);
        when(userRepository.updateUserByID(any(UserModel.class))).thenReturn(updatedUserModel);
        when(userMapper.toDTO(any(UserModel.class))).thenReturn(userDTO);

        Optional<UserDTO> result = userService.updateUser("test-123", userDTO);

        assertTrue(result.isPresent());
        assertEquals("Chihiro", result.get().getName());
        assertEquals("Shark", result.get().getLogin());
        assertEquals("pass123", result.get().getPassword());
    }

    @Test
    void testUpdateUserNoChanges() {
        when(userRepository.getUserById("test-123")).thenReturn(Optional.of(userModel));
        when(userMapper.toModel(userDTO)).thenReturn(userModel);
        when(userRepository.updateUserByID(any(UserModel.class))).thenReturn(userModel);
        when(userMapper.toDTO(any(UserModel.class))).thenReturn(userDTO);

        Optional<UserDTO> result = userService.updateUser("test-123", userDTO);

        assertTrue(result.isPresent());
        assertEquals(userDTO.getName(), result.get().getName());
        assertEquals(userDTO.getLogin(), result.get().getLogin());
        verify(userRepository, times(1)).updateUserByID(any(UserModel.class));
    }
}