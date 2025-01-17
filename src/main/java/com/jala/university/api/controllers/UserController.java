package com.jala.university.api.controllers;

import com.jala.university.core.security.PassEncoder;
import com.jala.university.core.services.UserService;
import com.jala.university.core.utils.UserValidator;
import com.jala.university.data.dto.LoginDTO;
import com.jala.university.data.dto.UserDTO;

import com.jala.university.data.models.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        log.info("Fetching all users");
        return userService.getUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        log.info("Registering a new user");
        userValidator.validate(userDTO);
        userDTO.setPassword(PassEncoder.passwordEncoder().encode(userDTO.getPassword()));
        UserDTO registeredUser = userService.createUser(userDTO);
        String responseMessage = "User " + registeredUser.getLogin() + " registered successfully with ID: " + registeredUser.getId();
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateUser(@RequestHeader("login") String login, @RequestHeader("password") String password) {
        LoginDTO loginEntry = new LoginDTO();
        loginEntry.setLogin(login);
        loginEntry.setPassword(password);
        UserModel userToValidate = userService.login(loginEntry);
        if(userToValidate==null)return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userToValidate.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        userValidator.validateGet(id);
        log.info("Fetching user with ID: {}", id);
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable("login") String login) {
        log.info("Fetching user with login: {}", login);
        userValidator.validateUserExistsLogin(login);
        return userService.getByLogin(login)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
        userValidator.validateUpdate(id, userDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("root") && !authentication.getName().equals(userService.getById(id).get().getLogin())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userDTO.setPassword(PassEncoder.passwordEncoder().encode(userDTO.getPassword()));
        }
        return userService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        log.info("Deleting user with ID: {}", id);
        userValidator.validateDelete(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("root") && !authentication.getName().equals(userService.getById(id).get().getLogin())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            log.info("User with ID: {} deleted successfully", id);
            return ResponseEntity.ok().body(java.util.Map.of("message", "User deleted successfully"));
        } else {
            log.warn("User with ID: {} not found for deletion", id);
            return ResponseEntity.status(404).body(java.util.Map.of("message", "User not found"));
        }
    }
}