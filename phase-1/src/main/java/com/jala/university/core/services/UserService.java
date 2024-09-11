package com.jala.university.core.services;

import com.jala.university.core.utils.UserMapper;
import com.jala.university.data.dto.UserDTO;
import com.jala.university.data.models.UserModel;
import com.jala.university.data.repositories.MySqlRepository;
import com.jala.university.data.repositories.MySqlUserRepositoryImpl;
import com.jala.university.data.repositories.Repository;
import com.jala.university.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final Repository<UserModel, String, String> userRepository;

    @Autowired
    public UserService(UserMapper userMapper, MySqlUserRepositoryImpl mySqlRepository, @Value("${database.type}") String databaseType){
        this.userMapper = userMapper;
        this.userRepository = new Repository<>(mySqlRepository, databaseType);
    }

    public List<UserDTO> getUsers() {
        return StreamSupport.stream(userRepository.findAllUsers().spliterator(), false)
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        UserModel user = userMapper.toModel(userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }

    public Optional<UserDTO> getById(String id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    public Optional<UserDTO> getByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toDTO);
    }

    @Transactional
    public Optional<UserDTO> updateUser(String id, UserDTO userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    UserModel updatedUser = userMapper.toModel(userDetails);
                    updatedUser.setId(existingUser.getId());
                    return userMapper.toDTO(userRepository.updateUser(updatedUser));
                });
    }

    @Transactional
    public boolean deleteUser(String id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserModel existingUser = user.get();
            userRepository.deleteUser(existingUser);
            return true;
        }
        return false;
    }
}