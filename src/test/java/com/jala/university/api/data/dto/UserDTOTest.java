package com.jala.university.api.data.dto;

import com.jala.university.data.dto.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDTOTest {

    @Test
    public void testUserDTOConstructorAndGetters() {
        String expectedId = "1";
        String expectedName = "Baby Shark";
        String expectedLogin = "Baby Shark";
        String expectedPassword = "securePassword";

        UserDTO userDTO = new UserDTO(expectedId, expectedName, expectedLogin, expectedPassword);

        assertEquals(expectedId, userDTO.getId());
        assertEquals(expectedName, userDTO.getName());
        assertEquals(expectedLogin, userDTO.getLogin());
        assertEquals(expectedPassword, userDTO.getPassword());
    }

    @Test
    public void testUserDTOSetters() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("2");
        userDTO.setName("Shark");
        userDTO.setLogin("shark");
        userDTO.setPassword("anotherPass");

        assertEquals("2", userDTO.getId());
        assertEquals("Shark", userDTO.getName());
        assertEquals("shark", userDTO.getLogin());
        assertEquals("anotherPass", userDTO.getPassword());
    }

    @Test
    public void testUserDTOToString() {
        UserDTO userDTO = new UserDTO("3", "Abby", "abby", "password123");

        String result = userDTO.toString();

        String expectedString = "UserDTO{id='3', name='Abby', login='abby', password='[PROTECTED]'}";
        assertEquals(expectedString, result);
    }
}