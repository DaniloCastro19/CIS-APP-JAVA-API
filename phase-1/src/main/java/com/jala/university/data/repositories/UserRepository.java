package com.jala.university.data.repositories;


import com.jala.university.core.models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserModel, String> {
    public abstract Optional<UserModel> findById(String id);
}
