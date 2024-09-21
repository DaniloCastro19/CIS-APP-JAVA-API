package com.jala.university.core.utils;

import com.jala.university.core.exception.BadRequestException;
import com.jala.university.core.exception.NotFoundException;
import com.jala.university.core.exception.RequestException;
import com.jala.university.core.services.UserService;
import com.jala.university.data.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class UserValidator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    public void validate(UserDTO userDTO) {
        validateUserNotNull(userDTO);
        validateLogin(userDTO.getLogin());
        validatePassword(userDTO.getPassword());
    }

    public void validateUpdate(String id, UserDTO userDTO) {
        validateUserExists(id);
        validateUserNotNull(userDTO);
        if (userDTO.getLogin() != null) {
            validateLogin(userDTO.getLogin());
        }
        if (userDTO.getPassword() != null) {
            validatePassword(userDTO.getPassword());
        }
    }

    public void validateDelete(String id){
        validateUserExists(id);
    }

    public void validateGet(String id){
        validateUserExists(id);
    }

    public void validateUserExistsLogin(String login) {
        userService.getByLogin(login)
                .orElseThrow(() -> new NotFoundException("P-404", "User with login " + login + " not found"));
    }

    private void validateUserExists(String id) {
        userService.getById(id)
                .orElseThrow(() -> new NotFoundException("P-404", "User with ID " + id + " not found"));
    }

    private void validateUserNotNull(UserDTO userDTO) {
        if (userDTO == null) {
            throw new RequestException("P-400", "User data cannot be null");
        }
    }

    public void validateLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new RequestException("P-400", "Login cannot be empty");
        }
        if (login.length() < 3) {
            throw new RequestException("P-400", "Login must be at least 3 characters long");
        }
        if (!login.matches("^[a-zA-Z0-9._-]{3,}$")) {
            throw new RequestException("P-400", "Login must contain only alphanumeric characters and dots, dashes or underscores");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new RequestException("P-400", "Password cannot be empty");
        }
        if (password.length() < 6) {
            throw new RequestException("P-400", "Password must be at least 6 characters long");
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")) {
            throw new RequestException("P-400", "Password must contain at least one letter and one number");
        }
    }
}