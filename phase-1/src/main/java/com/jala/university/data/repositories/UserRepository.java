package com.jala.university.data.repositories;
import java.util.List;

import com.jala.university.data.models.UserModel;

import java.util.Optional;

public interface UserRepository{
    UserModel createUser(UserModel user);
    Optional<UserModel> getUserByLogin(String login);
    Optional<UserModel> getUserById(String id);
    List<UserModel> getUsers();
    UserModel updateUserByID(UserModel user);
    void deleteUser(UserModel user);
}
