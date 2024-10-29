package com.jala.university.api.security;

import com.jala.university.core.security.UserApiDetailsService;
import com.jala.university.core.services.UserService;
import com.jala.university.data.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserApiDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserApiDetailsService userApiDetailsService;

    @Test
    void shouldLoadAdminUser() {
        UserDetails userDetails = userApiDetailsService.loadUserByUsername("root");

        assertNotNull(userDetails);
        assertEquals("root", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void shouldLoadRegularUser() {
        String username = "testuser";
        String password = "testpass";
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(username);
        userDTO.setPassword(password);
        when(userService.getByLogin(username)).thenReturn(Optional.of(userDTO));

        UserDetails userDetails = userApiDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void shouldThrowExceptionForUnknownUser() {
        String username = "unknownuser";
        when(userService.getByLogin(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userApiDetailsService.loadUserByUsername(username),
                "Should throw UsernameNotFoundException for unknown user");
    }
}