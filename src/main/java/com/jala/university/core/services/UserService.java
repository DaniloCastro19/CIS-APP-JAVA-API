package com.jala.university.core.services;

import com.jala.university.config.CacheConfig;
import com.jala.university.core.utils.UserMapper;
import com.jala.university.data.dto.UserDTO;
import com.jala.university.data.models.UserModel;
import com.jala.university.data.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserMapper userMapper,UserRepository userRepository){
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public List<UserDTO> getUsers() {
        return userRepository.getUsers().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        UserModel user = userMapper.toModel(userDTO);
        return userMapper.toDTO(userRepository.createUser(user));
    }

    @Cacheable(value = CacheConfig.USERS_INFO_CACHE, unless = "#result == null")
    public Optional<UserDTO> getById(String id) {
        log.info("accessing from the database");
        return userRepository.getUserById(id).map(userMapper::toDTO);
    }

    public Optional<UserDTO> getByLogin(String login) {
        return userRepository.getUserByLogin(login).map(userMapper::toDTO);
    }

    @Transactional
    public Optional<UserDTO> updateUser(String id, UserDTO userDetails) {
        return userRepository.getUserById(id)
                .map(existingUser -> {
                    UserModel updatedUser = userMapper.toModel(userDetails);
                    updatedUser.setId(existingUser.getId());
                    return userMapper.toDTO(userRepository.updateUserByID(updatedUser));
                });
    }

    @Transactional
    public boolean deleteUser(String id) {
        Optional<UserModel> user = userRepository.getUserById(id);
        if (user.isPresent()) {
            UserModel existingUser = user.get();
            userRepository.deleteUser(existingUser);
            return true;
        }
        return false;
    }
}