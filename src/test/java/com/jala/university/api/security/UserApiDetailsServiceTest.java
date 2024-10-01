package com.jala.university.api.security;

import com.jala.university.core.security.UserApiDetailsService;
import com.jala.university.core.services.UserService;
import com.jala.university.data.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserApiDetailsServiceTest {

    @InjectMocks
    private UserApiDetailsService userApiDetailsService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "unknownUser";

        when(userService.getByLogin(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userApiDetailsService.loadUserByUsername(username)
        );
        assertEquals("User not found: " + username, exception.getMessage());
    }
}