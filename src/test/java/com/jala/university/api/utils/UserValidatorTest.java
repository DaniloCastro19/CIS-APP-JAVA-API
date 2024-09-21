package com.jala.university.api.utils;

import com.jala.university.core.exception.RequestException;
import com.jala.university.core.utils.UserValidator;
import com.jala.university.data.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    void shouldThrowExceptionWhenUserDTOIsNull() {
        assertThrows(RequestException.class,
                () -> userValidator.validate(null),
                "Should throw exception when UserDTO is null");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("Shark");
        userDTO.setPassword(null);

        assertThrows(RequestException.class,
                () -> userValidator.validate(userDTO),
                "Should throw exception when password is null");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsEmpty() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("Shark");
        userDTO.setPassword("");

        assertThrows(RequestException.class,
                () -> userValidator.validate(userDTO),
                "Should throw exception when password is empty");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsTooShort() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("Shark");
        userDTO.setPassword("12345");

        assertThrows(RequestException.class,
                () -> userValidator.validate(userDTO),
                "Should throw exception when password is less than 6 characters");
    }

    @Test
    void shouldThrowExceptionWhenLoginIsNull() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(null);
        userDTO.setPassword("pass123");

        assertThrows(RequestException.class,
                () -> userValidator.validate(userDTO),
                "Should throw exception when login is null");
    }

    @Test
    void shouldThrowExceptionWhenLoginIsEmpty() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("");
        userDTO.setPassword("pass123");

        assertThrows(RequestException.class,
                () -> userValidator.validate(userDTO),
                "Should throw exception when login is empty");
    }

    @Test
    void shouldThrowExceptionWhenLoginIsTooShort() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("bs");
        userDTO.setPassword("pass123");

        assertThrows(RequestException.class,
                () -> userValidator.validate(userDTO),
                "Should throw exception when login is less than 3 characters");
    }

    @Test
    void shouldNotThrowExceptionForValidUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("Shark");
        userDTO.setPassword("pass123");

        assertDoesNotThrow(() -> userValidator.validate(userDTO),
                "Should not throw exception for valid UserDTO");
    }

    @Test
    void shouldThrowExceptionWhenUserDTOIsNullInUpdate() {
        assertThrows(RequestException.class,
                () -> userValidator.validateUpdate(null),
                "Should throw exception when UserDTO is null in update");
    }

    @Test
    void shouldThrowExceptionWhenLoginIsTooShortInUpdate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("bs");

        assertThrows(RequestException.class,
                () -> userValidator.validateUpdate(userDTO),
                "Should throw exception when login is less than 3 characters in update");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsTooShortInUpdate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("12345");

        assertThrows(RequestException.class,
                () -> userValidator.validateUpdate(userDTO),
                "Should throw exception when password is less than 6 characters in update");
    }

    @Test
    void shouldAcceptNullLoginInUpdate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(null);
        userDTO.setPassword("pass123");

        assertDoesNotThrow(() -> userValidator.validateUpdate(userDTO),
                "Should accept null login in update");
    }

    @Test
    void shouldAcceptNullPasswordInUpdate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("Shark");
        userDTO.setPassword(null);

        assertDoesNotThrow(() -> userValidator.validateUpdate(userDTO),
                "Should accept null password in update");
    }

    @Test
    void shouldAcceptValidLoginAndPasswordInUpdate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("Shark");
        userDTO.setPassword("pass123");

        assertDoesNotThrow(() -> userValidator.validateUpdate(userDTO),
                "Should accept valid login and password in update");
    }
}