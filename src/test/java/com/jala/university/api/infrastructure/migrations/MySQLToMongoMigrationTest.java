package com.jala.university.api.infrastructure.migrations;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class MySQLToMongoMigrationTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.4"))
            .withDatabaseName("sd3")
            .withUsername("root")
            .withPassword("sd5");

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.0"));

    private MongoClient mongoClient;
    private Connection mysqlConnection;

    @BeforeAll
    static void initAll() {
        mysqlContainer.start();
        mongoDBContainer.start();
    }

    @BeforeEach
    void setUp() {
        mongoClient = null;
        mysqlConnection = null;
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (mongoClient != null) {
            mongoClient.close();
        }
        if (mysqlConnection != null && !mysqlConnection.isClosed()) {
            mysqlConnection.close();
        }
    }

    @AfterAll
    static void tearDownAll() {
        mysqlContainer.stop();
        mongoDBContainer.stop();
    }

    @Test
    void testMySQLConnection() {
        assertDoesNotThrow(() -> {
            mysqlConnection = DriverManager.getConnection(
                    mysqlContainer.getJdbcUrl(),
                    mysqlContainer.getUsername(),
                    mysqlContainer.getPassword()
            );
            assertNotNull(mysqlConnection);
            assertTrue(mysqlConnection.isValid(1000));
        }, "Connection to MySQL should be established successfully");
    }

    @Test
    void testMongoDBConnection() {
        assertDoesNotThrow(() -> {
            mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
            MongoDatabase database = mongoClient.getDatabase("sd3");
            assertNotNull(database);
            database.listCollectionNames().first();
        }, "Connection to MongoDB should be established successfully");
    }

    @Test
    void testInvalidMySQLCredentials() {
        Exception exception = assertThrows(SQLException.class, () -> {
            DriverManager.getConnection(
                    mysqlContainer.getJdbcUrl(),
                    "wrong_user",
                    "wrong_password"
            );
        });
        assertTrue(exception.getMessage().contains("Access denied"));
    }

    @Test
    void testConnectionClosing() throws SQLException {
        mysqlConnection = DriverManager.getConnection(
                mysqlContainer.getJdbcUrl(),
                mysqlContainer.getUsername(),
                mysqlContainer.getPassword()
        );
        mysqlConnection.close();
        assertTrue(mysqlConnection.isClosed());

        mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
        mongoClient.close();
        assertThrows(Exception.class, () ->
                mongoClient.getDatabase("sd3").listCollectionNames().first()
        );
    }
}