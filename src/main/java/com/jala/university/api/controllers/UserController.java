package com.jala.university.api.controllers;

import com.jala.university.core.security.PassEncoder;
import com.jala.university.core.services.UserService;
import com.jala.university.data.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        log.info("Fetching all users");
        return userService.getUsers();
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        log.info("Creating a new user");
        userDTO.setPassword(PassEncoder.passwordEncoder().encode(userDTO.getPassword()));
        return userService.createUser(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        log.info("Fetching user with ID: {}", id);
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable("login") String login) {
        log.info("Fetching user with login: {}", login);
        return userService.getByLogin(login)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
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