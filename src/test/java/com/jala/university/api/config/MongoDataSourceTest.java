package com.jala.university.api.config;

import com.jala.university.config.MongoDataSource;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoDataSourceTest {

    @Mock
    private MongoProperties mongoProperties;

    @Mock
    private MongoClient mongoClient;

    @InjectMocks
    private MongoDataSource mongoDataSource;

    @Test
    void shouldCreateMongoClient() {
        String expectedUri = "mongodb://localhost:27017/sd3";
        when(mongoProperties.getUri()).thenReturn(expectedUri);

        try {
            MongoClient result = mongoDataSource.mongoClient();

            assertNotNull(result, "MongoClient should not be null");
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void constructorShouldInitializeWithProperties() {
        MongoDataSource dataSource = new MongoDataSource(mongoProperties);

        assertNotNull(dataSource, "MongoDataSource should be initialized");
    }

    @Test
    void shouldThrowExceptionWithInvalidUri() {
        when(mongoProperties.getUri()).thenReturn("invalid-uri");

        assertThrows(IllegalArgumentException.class,
                () -> mongoDataSource.mongoClient(),
                "Should throw exception for invalid URI");
    }

    @Test
    void shouldUseCustomConverterSettings() {
        // Arrange
        when(mongoProperties.getDatabase()).thenReturn("testdb");

        // Act
        MongoTemplate template = mongoDataSource.mongoTemplate(mongoClient);
        MappingMongoConverter converter = (MappingMongoConverter) template.getConverter();

        // Assert
        assertNotNull(converter, "Converter should not be null");
        assertTrue(converter instanceof MappingMongoConverter,
                "Converter should be instance of MappingMongoConverter");
    }

    @Test
    void shouldHandleEmptyDatabaseName() {
        when(mongoProperties.getDatabase()).thenReturn("");

        assertThrows(IllegalArgumentException.class,
                () -> mongoDataSource.mongoTemplate(mongoClient),
                "Should throw exception for empty database name");
    }

    @Test
    void shouldHandleNullDatabaseName() {
        when(mongoProperties.getDatabase()).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> mongoDataSource.mongoTemplate(mongoClient),
                "Should throw exception for null database name");
    }

    @Test
    void shouldHandleNullMongoClient() {
        when(mongoProperties.getDatabase()).thenReturn("sd3");

        assertThrows(IllegalArgumentException.class,
                () -> mongoDataSource.mongoTemplate(null),
                "Should throw exception for null MongoClient");
    }
}