package com.jala.university.data.repositories;

import com.jala.university.data.models.UserModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name= "database.type", havingValue = "mysql")
public class MySqlUserRepositoryImpl implements UserRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserModel createUser(UserModel user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<UserModel> getUserByLogin(String login) {
        String query = "SELECT u FROM UserModel u WHERE u.login =:login";
        List<UserModel> result= entityManager.createQuery(query, UserModel.class)
                .setParameter("login", login)
                .getResultList();
        return Optional.of(result.get(0));
    }

    @Override
    public Optional<UserModel> getUserById(String id) {
        return Optional.ofNullable(entityManager.find(UserModel.class, id));

    }

    @Override
    public List<UserModel> getUsers() {
        return entityManager.createQuery("SELECT u FROM UserModel u", UserModel.class).getResultList();
    }

    @Override
    public UserModel updateUserByID(UserModel user) {
        return entityManager.merge(user);
    }

    @Override
    public void deleteUser(UserModel user) {
        entityManager.remove(user);
    }
}
