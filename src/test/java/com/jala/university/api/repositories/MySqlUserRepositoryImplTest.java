package com.jala.university.api.repositories;

import com.jala.university.data.models.UserModel;
import com.jala.university.data.repositories.MySqlUserRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MySqlUserRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private MySqlUserRepositoryImpl mySqlUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldPersistUser() {
        UserModel user = new UserModel();
        user.setName("John Doe");
        user.setLogin("johndoe");
        user.setPassword("securepassword");
        mySqlUserRepository.createUser(user);
        verify(entityManager, times(1)).persist(user);
    }

    @Test
    void getUserByLogin_ShouldReturnUser_WhenUserExists() {
        UserModel user = new UserModel();
        user.setLogin("johndoe");
        String query = "SELECT u FROM UserModel u WHERE u.login =:login";
        TypedQuery<UserModel> typedQueryMock = mock(TypedQuery.class);

        when(entityManager.createQuery(query, UserModel.class)).thenReturn(typedQueryMock);

        when(typedQueryMock.setParameter("login", "johndoe")).thenReturn(typedQueryMock);

        when(typedQueryMock.getResultList()).thenReturn(Collections.singletonList(user));

        Optional<UserModel> result = mySqlUserRepository.getUserByLogin("johndoe");
        assertTrue(result.isPresent());
        assertEquals("johndoe", result.get().getLogin());
    }




    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        UserModel user = new UserModel();
        user.setId("123");

        when(entityManager.find(UserModel.class, "123")).thenReturn(user);

        Optional<UserModel> result = mySqlUserRepository.getUserById("123");

        assertTrue(result.isPresent());
        assertEquals("123", result.get().getId());
    }

    @Test
    void getUserById_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        when(entityManager.find(UserModel.class, "unknown")).thenReturn(null);

        Optional<UserModel> result = mySqlUserRepository.getUserById("unknown");

        assertFalse(result.isPresent());
    }

    @Test
    void getUsers_ShouldReturnUserList() {
        UserModel user = new UserModel();
        List<UserModel> userList = Collections.singletonList(user);
        TypedQuery<UserModel> typedQueryMock = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT u FROM UserModel u", UserModel.class)).thenReturn(typedQueryMock);

        when(typedQueryMock.getResultList()).thenReturn(userList);

        List<UserModel> result = mySqlUserRepository.getUsers();
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }


    @Test
    void updateUserById_ShouldMergeUser() {
        UserModel user = new UserModel();
        user.setId("123");

        when(entityManager.merge(user)).thenReturn(user);

        UserModel result = mySqlUserRepository.updateUserByID(user);

        verify(entityManager, times(1)).merge(user);

        assertEquals(user, result);
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        UserModel user = new UserModel();
        user.setId("123");
        mySqlUserRepository.deleteUser(user);
        verify(entityManager, times(1)).remove(user);
    }
}
