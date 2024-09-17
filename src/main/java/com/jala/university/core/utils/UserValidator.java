package com.jala.university.core.utils;

import com.jala.university.core.exception.RequestException;
import com.jala.university.data.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public void validate(UserDTO userDTO) {
        if (userDTO == null) {
            throw new RequestException("P-400", "Data user is null");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new RequestException("P-400", "Password cannot be empty");
        }
        if (userDTO.getPassword().length() < 6) {
            throw new RequestException("P-400", "Password must be at least 6 characters long");
        }
        if (userDTO.getLogin() == null || userDTO.getLogin().isEmpty()) {
            throw new RequestException("P-400", "Login cannot be empty");
        }
        if (userDTO.getLogin().length() < 3) {
            throw new RequestException("P-400", "Login must be at least 3 characters long");
        }
    }

    public void validateUpdate(UserDTO userDTO) {
        if (userDTO == null) {
            throw new RequestException("P-400", "Data user is null");
        }
        if (userDTO.getLogin() != null && userDTO.getLogin().length() < 3) {
            throw new RequestException("P-400", "Login must be at least 3 characters long");
        }
        if (userDTO.getPassword() != null && userDTO.getPassword().length() < 6) {
            throw new RequestException("P-400", "Password must be at least 6 characters long");
        }
    }
}