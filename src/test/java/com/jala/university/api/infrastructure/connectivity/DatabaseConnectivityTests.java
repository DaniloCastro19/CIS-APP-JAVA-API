package com.jala.university.api.infrastructure.connectivity;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Test;

public class DatabaseConnectivityTests {
    private static final String MONGO_URI = "mongodb://localhost:27017/sd3";
    private static final String MONGO_DATABASE = "sd3";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3307/sd3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sd5";
    private static final String HOST = "localhost";
    private static final int PORT = 27017;

    @Test
    public void testMongoDBConnection() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(MONGO_DATABASE);
            assertNotNull(database, "Database connection should not be null");

            boolean isAlive = database.runCommand(new org.bson.Document("ping", 1))
                    .containsKey("ok");
            assertTrue(isAlive, "MongoDB should be responsive");

        } catch (Exception e) {
            fail("Failed to connect to MongoDB: " + e.getMessage());
        }
    }

    @Test
    public void testMongoDBConnectionWithAuth() {
        String connectionString = String.format(
                "mongodb://%s:%s@%s:%d/%s",
                USERNAME, PASSWORD, HOST, PORT, MONGO_DATABASE
        );

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase db = mongoClient.getDatabase(MONGO_DATABASE);
            assertNotNull(db, "Database connection should not be null");
        } catch (Exception e) {
            fail("Failed to connect to MongoDB with authentication: " + e.getMessage());
        }
    }

    @Test
    public void testMongoDBConnectionWithOptions() {
        ConnectionString connString = new ConnectionString(
                MONGO_URI + "?" +
                        "maxPoolSize=20&" +
                        "connectTimeoutMS=2000&" +
                        "socketTimeoutMS=5000"
        );

        try (MongoClient mongoClient = MongoClients.create(connString)) {
            MongoDatabase database = mongoClient.getDatabase(MONGO_DATABASE);
            assertNotNull(database, "Database connection should not be null");

            boolean isAlive = database.runCommand(new org.bson.Document("ping", 1))
                    .containsKey("ok");
            assertTrue(isAlive, "MongoDB with options should be responsive");
        } catch (Exception e) {
            fail("Failed to connect to MongoDB with options: " + e.getMessage());
        }
    }

    @Test
    public void testMySQLConnection() {
        try (Connection connection = DriverManager.getConnection(
                MYSQL_URL, USERNAME, PASSWORD)) {

            assertNotNull(connection, "MySQL connection should not be null");
            assertTrue(connection.isValid(5), "MySQL connection should be valid");

        } catch (Exception e) {
            fail("Failed to connect to MySQL: " + e.getMessage());
        }
    }

    @Test
    public void testMySQLConnectionWithOptions() {
        String urlWithOptions = MYSQL_URL +
                "?useSSL=false" +
                "&allowPublicKeyRetrieval=true" +
                "&serverTimezone=UTC" +
                "&maxPoolSize=20" +
                "&connectTimeout=2000";

        try (Connection connection = DriverManager.getConnection(
                urlWithOptions, USERNAME, PASSWORD)) {

            assertNotNull(connection, "MySQL connection with options should not be null");
            assertTrue(connection.isValid(5), "MySQL connection with options should be valid");

        } catch (Exception e) {
            fail("Failed to connect to MySQL with options: " + e.getMessage());
        }
    }

    @Test
    public void testMySQLConnectionProperties() {
        try (Connection connection = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD)) {
            assertNotNull(connection.getCatalog(), "Database name should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
            assertTrue(connection.getAutoCommit(), "AutoCommit should be enabled by default");

            String currentDatabase = connection.getCatalog();
            assertEquals("sd3", currentDatabase, "Should be connected to the correct database");

        } catch (Exception e) {
            fail("Failed to verify MySQL connection properties: " + e.getMessage());
        }
    }
}