package com.jala.university.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoDataSource {
    private final MongoProperties mongoProperties;

    public MongoDataSource(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

    @ConfigurationProperties("spring.data.mongodb")
    @Bean
    public MongoClient mongoClient(){
        return MongoClients.create(mongoProperties.getUri());
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoClient(),  mongoProperties.getDatabase());
    }

}
