package com.jala.university.data.repositories;

import com.jala.university.data.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name= "database.type", havingValue = "mongo")
public class MongoUserRepositoryImpl implements UserRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public UserModel createUser(UserModel user) {
        return mongoTemplate.save(user);
    }

    @Override
    public Optional<UserModel> getUserByLogin(String login) {
        Query query = new Query();
        query.addCriteria(Criteria.where("login").is(login));
        UserModel user = mongoTemplate.findOne(query, UserModel.class);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<UserModel> getUserById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, UserModel.class));
    }

    @Override
    public List<UserModel> getUsers() {
        return mongoTemplate.findAll(UserModel.class);
    }

    @Override
    public UserModel updateUserByID(UserModel user) {
        return mongoTemplate.save(user);
    }

    @Override
    public void deleteUser(UserModel user) {
        mongoTemplate.remove(user);
    }
}
