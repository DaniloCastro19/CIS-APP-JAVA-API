package com.jala.university.api.controllers;

import com.jala.university.core.models.UserModel;
import com.jala.university.core.security.PassEncoder;
import com.jala.university.core.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping
    public ArrayList<UserModel> getUsers(){
        return this.userService.getUsers();
    }

    @PostMapping()
    public UserModel createUser(@RequestBody UserModel user){
        user.setPassword(PassEncoder.passwordEncoder().encode(user.getPassword()));
        logger.info("user created");
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable String id) {
        Optional<UserModel> user = userService.getById(id);
        logger.info("a user was obtained: ");
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserModel> getUserByLogin(@PathVariable("login") String login) {
        Optional<UserModel> user = userService.getByLogin(login);
        logger.info("a user logged in");
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable String id, @RequestBody UserModel userDetails) {
        logger.info("Updating user with ID: {}", id);
        UserModel updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            logger.info("User updated with ID: {}", id);
            return ResponseEntity.ok(updatedUser);
        } else {
            logger.warn("User not found for update with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        logger.info("Deleting user with ID: {}", id);
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            logger.info("User deleted with ID: {}", id);
            return ResponseEntity.ok().body(new HashMap<String, String>() {{
                put("message", "user deleted successfully");
            }});
        } else {
            logger.warn("User not found for deletion with ID: {}", id);
            return ResponseEntity.status(404).body(new HashMap<String, String>() {{
                put("message", "User not found");
            }});
        }
    }
}
