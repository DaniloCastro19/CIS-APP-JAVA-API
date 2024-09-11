package com.jala.university.core.services;

import com.jala.university.core.utils.UserMapper;
import com.jala.university.core.utils.UserValidator;
import com.jala.university.data.dto.UserDTO;
import com.jala.university.data.models.UserModel;
import com.jala.university.data.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO userDTO) {
        UserModel user = userMapper.toModel(userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO registerUser(UserDTO userDTO) {
        UserModel user = userMapper.toModel(userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }

    public Optional<UserDTO> getById(String id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    public Optional<UserDTO> getByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toDTO);
    }

    public Optional<UserDTO> updateUser(String id, UserDTO userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    UserModel updatedUser = userMapper.toModel(userDetails);
                    updatedUser.setId(existingUser.getId());
                    return userMapper.toDTO(userRepository.save(updatedUser));
                });
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}