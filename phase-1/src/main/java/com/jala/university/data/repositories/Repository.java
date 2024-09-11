package com.jala.university.data.repositories;

import com.jala.university.data.models.UserModel;

import java.util.List;
import java.util.Optional;

public class Repository<T, ID, Login>{
    private MySqlRepository<T,ID,Login> mySqlRepository;
    private String databaseType;

    public Repository(MySqlRepository<T, ID, Login> mySqlRepository, String databaseType) {
        this.mySqlRepository = mySqlRepository;
        this.databaseType = databaseType;
    }

    public T save(T entity){
        if ("mysql".equalsIgnoreCase(databaseType)){
            return  mySqlRepository.createUser(entity);
        }
        throw new UnsupportedOperationException("Database type not supported");
    }

    public Optional<T> findByLogin(Login login){
        if ("mysql".equalsIgnoreCase(databaseType)){
            return  mySqlRepository.getUserByLogin(login);
        }
        throw new UnsupportedOperationException("Database type not supported");
    }

    public Optional<T> findById(ID id){
        if ("mysql".equalsIgnoreCase(databaseType)){
            return  mySqlRepository.getUserById(id);
        }
        throw new UnsupportedOperationException("Database type not supported");
    }

    public List<T> findAllUsers(){
        if ("mysql".equalsIgnoreCase(databaseType)){
            return  mySqlRepository.getUsers();
        }
        throw new UnsupportedOperationException("Database type not supported");
    }

    public UserModel updateUser(T user){
        if ("mysql".equalsIgnoreCase(databaseType)){
            return  mySqlRepository.updateUserByID(user);
        }
        throw new UnsupportedOperationException("Database type not supported");
    }

    public void deleteUser(T user){
        if ("mysql".equalsIgnoreCase(databaseType)){
            mySqlRepository.deleteUser(user);
        }else{
            throw new UnsupportedOperationException("Database type not supported");
        }
    }


}
