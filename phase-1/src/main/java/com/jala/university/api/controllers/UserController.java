package com.jala.university.api.controllers;

import com.jala.university.core.security.PassEncoder;
import com.jala.university.core.services.UserService;
import com.jala.university.data.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return userService.getUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        userDTO.setPassword(PassEncoder.passwordEncoder().encode(userDTO.getPassword()));
        UserDTO registeredUser = userService.registerUser(userDTO);
        String responseMessage = "User " + registeredUser.getLogin() + " registered successfully with ID: " + registeredUser.getId();
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        userDTO.setPassword(PassEncoder.passwordEncoder().encode(userDTO.getPassword()));
        return userService.createUser(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable("login") String login) {
        return userService.getByLogin(login)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userDTO.setPassword(PassEncoder.passwordEncoder().encode(userDTO.getPassword()));
        }

        return userService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok().body(java.util.Map.of("message", "User deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(java.util.Map.of("message", "User not found"));
        }
    }
}