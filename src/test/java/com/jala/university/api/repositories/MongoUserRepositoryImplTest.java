package com.jala.university.api.repositories;

import com.jala.university.data.models.UserModel;
import com.jala.university.data.repositories.MongoUserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MongoUserRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private MongoUserRepositoryImpl userRepository;

    private UserModel testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserModel();
        testUser.setLogin("testLogin");
        testUser.setId("testId");
    }

    @Test
    void createUser_shouldSaveUserAndReturnIt() {
        when(mongoTemplate.save(any(UserModel.class))).thenReturn(testUser);

        UserModel savedUser = userRepository.createUser(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser.getId(), savedUser.getId());
        verify(mongoTemplate, times(1)).save(testUser);
    }

    @Test
    void getUserById_shouldReturnUserIfExists() {
        when(mongoTemplate.findById("testId", UserModel.class)).thenReturn(testUser);

        Optional<UserModel> user = userRepository.getUserById("testId");

        assertTrue(user.isPresent());
        assertEquals(testUser.getId(), user.get().getId());
    }

    @Test
    void getUserById_shouldReturnEmptyIfUserNotFound() {
        when(mongoTemplate.findById("nonExistentId", UserModel.class)).thenReturn(null);

        Optional<UserModel> user = userRepository.getUserById("nonExistentId");

        assertFalse(user.isPresent());
    }

    @Test
    void getUsers_shouldReturnListOfUsers() {
        UserModel user1 = new UserModel();
        UserModel user2 = new UserModel();
        when(mongoTemplate.findAll(UserModel.class)).thenReturn(Arrays.asList(user1, user2));

        List<UserModel> users = userRepository.getUsers();

        assertEquals(2, users.size());
        verify(mongoTemplate, times(1)).findAll(UserModel.class);
    }

    @Test
    void updateUserByID_shouldSaveAndReturnUser() {
        when(mongoTemplate.save(any(UserModel.class))).thenReturn(testUser);

        UserModel updatedUser = userRepository.updateUserByID(testUser);

        assertNotNull(updatedUser);
        assertEquals(testUser.getId(), updatedUser.getId());
        verify(mongoTemplate, times(1)).save(testUser);
    }

    @Test
    void deleteUser_shouldRemoveUser() {
        userRepository.deleteUser(testUser);
        verify(mongoTemplate, times(1)).remove(testUser);
    }
}
