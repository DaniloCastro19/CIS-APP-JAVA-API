package com.jala.university.core.services;


import com.jala.university.core.models.UserModel;
import com.jala.university.data.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;

    public UserModel createUser(UserModel request){
        UserModel newUser = new UserModel();
        newUser.setName(request.getName());
        newUser.setLogin(request.getLogin());
        newUser.setPassword(request.getPassword());

        return userRepository.save(newUser);
    }
}
