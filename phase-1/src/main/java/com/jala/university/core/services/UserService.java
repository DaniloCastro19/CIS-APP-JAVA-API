package com.jala.university.core.services;


import com.jala.university.core.models.UserModel;
import com.jala.university.data.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;

    public ArrayList<UserModel> getUsers() {
        return (ArrayList<UserModel>) userRepository.findAll();
    }

    public UserModel createUser(UserModel request) {
        UserModel newUser = new UserModel();
        newUser.setName(request.getName());
        newUser.setLogin(request.getLogin());
        newUser.setPassword(request.getPassword());

        return userRepository.save(newUser);
    }

    public Optional<UserModel> getById(String id) {
        return userRepository.findById(id);
    }

    public Optional<UserModel> getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public UserModel updateUser(String id, UserModel userDetails) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserModel existingUser = user.get();
            existingUser.setName(userDetails.getName());
            existingUser.setLogin(userDetails.getLogin());
            existingUser.setPassword(userDetails.getPassword());
            return userRepository.save(existingUser);
        }
        return null;
    }
}
