package com.jala.university.data.repositories;
import java.util.List;

import com.jala.university.data.models.UserModel;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface MySqlRepository<T, ID, Login>{
    T createUser(T user);
    Optional<T> getUserByLogin(Login login);
    Optional<T> getUserById(ID id);
    List<T> getUsers();
    UserModel updateUserByID(T user);
    void deleteUser(T user);
}
