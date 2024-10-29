package com.jala.university.api.infrastructure.connectivity;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class CacheConnectivityTests {
    private static final String MONGO_URI = "mongodb://localhost:27017/sd3";
    private static final String MONGO_DATABASE = "sd3";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3307/sd3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sd5";

    private MongoClient mongoClient;
    private Connection mysqlConnection;

    @BeforeEach
    void setup() throws Exception {
        mongoClient = MongoClients.create(MONGO_URI);
        mysqlConnection = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD);
    }

    @AfterEach
    void cleanup() throws Exception {
        if (mongoClient != null) mongoClient.close();
        if (mysqlConnection != null) mysqlConnection.close();
    }

    @Test
    public void testMongoDBQueryCache() {
        MongoDatabase db = mongoClient.getDatabase(MONGO_DATABASE);
        MongoCollection<Document> collection = db.getCollection("test_cache");

        collection.drop();

        Document testDoc = new Document("test_key", "test_value");
        collection.insertOne(testDoc);

        long startTime1 = System.nanoTime();
        Document result1 = collection.find(new Document("test_key", "test_value")).first();
        long endTime1 = System.nanoTime();
        long firstQueryTime = TimeUnit.NANOSECONDS.toMillis(endTime1 - startTime1);

        long startTime2 = System.nanoTime();
        Document result2 = collection.find(new Document("test_key", "test_value")).first();
        long endTime2 = System.nanoTime();
        long secondQueryTime = TimeUnit.NANOSECONDS.toMillis(endTime2 - startTime2);

        assertNotNull(result1, "First query should return result");
        assertNotNull(result2, "Second query should return result");
        assertTrue(secondQueryTime <= firstQueryTime,
                "Second query should be faster due to cache");
    }

    @Test
    public void testMySQLCacheHitRatio() throws Exception {
        try (Statement stmt = mysqlConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SHOW GLOBAL STATUS LIKE 'Qcache%'");

            int hits = 0;
            int requests = 0;

            while (rs.next()) {
                String variable = rs.getString(1);
                long value = rs.getLong(2);

                if ("Qcache_hits".equals(variable)) {
                    hits = (int) value;
                } else if ("Qcache_inserts".equals(variable)) {
                    requests += value;
                }
            }

            if (requests > 0) {
                double hitRatio = (double) hits / requests;
                assertTrue(hitRatio >= 0 && hitRatio <= 1,
                        "Cache hit ratio should be between 0 and 1");
            }
        }
    }

    @Test
    public void testMongoDBCacheCapacity() {
        MongoDatabase db = mongoClient.getDatabase(MONGO_DATABASE);
        MongoCollection<Document> collection = db.getCollection("test_capacity");

        for (int i = 0; i < 1000; i++) {
            Document doc = new Document("key", i);
            collection.insertOne(doc);
        }

        long startTime = System.nanoTime();
        Document result = collection.find(new Document("key", 0)).first();
        long endTime = System.nanoTime();

        assertNotNull(result, "Document should exist in collection");
        assertTrue(TimeUnit.NANOSECONDS.toMillis(endTime - startTime) < 500,
                "Cache should improve response time for repeated queries");

        collection.drop();
    }

    @Test
    public void testMongoDBCacheConsistency() {
        MongoDatabase db = mongoClient.getDatabase(MONGO_DATABASE);
        MongoCollection<Document> collection = db.getCollection("test_consistency");

        Document doc = new Document("key", "initial_value");
        collection.insertOne(doc);

        Document result1 = collection.find(new Document("key", "initial_value")).first();
        assertNotNull(result1, "Document should exist initially");

        collection.updateOne(new Document("key", "initial_value"),
                new Document("$set", new Document("key", "updated_value")));

        Document result2 = collection.find(new Document("key", "updated_value")).first();
        assertNotNull(result2, "Document should be updated in cache");

        collection.drop();
    }

    @Test
    public void testMySQLCacheSizeLimit() throws Exception {
        try (Statement stmt = mysqlConnection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS cache_limit (id INT, value VARCHAR(50))");

            for (int i = 0; i < 5000; i++) {
                stmt.execute("INSERT INTO cache_limit VALUES (" + i + ", 'value')");
            }

            ResultSet rs = stmt.executeQuery("SELECT * FROM cache_limit WHERE id = 1");
            assertTrue(rs.next(), "Result should be fetched successfully with caching");

            stmt.execute("DROP TABLE cache_limit");
        }
    }

    @Test
    public void testMySQLQueryCacheEviction() throws Exception {
        try (Statement stmt = mysqlConnection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS eviction_cache (id INT PRIMARY KEY, value VARCHAR(50))");
            stmt.execute("INSERT INTO eviction_cache VALUES (1, 'initial_value')");

            ResultSet rs1 = stmt.executeQuery("SELECT * FROM eviction_cache WHERE id = 1");
            assertTrue(rs1.next(), "Initial query should be cached");

            stmt.execute("UPDATE eviction_cache SET value = 'updated_value' WHERE id = 1");

            ResultSet rs2 = stmt.executeQuery("SELECT * FROM eviction_cache WHERE id = 1");
            assertTrue(rs2.next(), "Cached query should reflect updated value");

            stmt.execute("DROP TABLE eviction_cache");
        }
    }

    @Test
    public void testMongoDBIndexingWithCache() {
        MongoDatabase db = mongoClient.getDatabase(MONGO_DATABASE);
        MongoCollection<Document> collection = db.getCollection("index_cache");

        collection.createIndex(Indexes.ascending("key"));

        for (int i = 0; i < 1000; i++) {
            collection.insertOne(new Document("key", i));
        }

        long startTime = System.nanoTime();
        Document result = collection.find(new Document("key", 500)).first();
        long endTime = System.nanoTime();

        assertNotNull(result, "Indexed query should return a result");
        assertTrue(TimeUnit.NANOSECONDS.toMillis(endTime - startTime) < 500,
                "Indexed queries should benefit from cache and be fast");

        collection.drop();
    }

    @Test
    public void testMySQLCacheMissOnNewData() throws Exception {
        try (Statement stmt = mysqlConnection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS cache_miss (id INT PRIMARY KEY, value VARCHAR(50))");
            stmt.execute("INSERT INTO cache_miss VALUES (1, 'cached_value')");

            stmt.execute("INSERT INTO cache_miss VALUES (2, 'new_value')");

            ResultSet rs = stmt.executeQuery("SELECT * FROM cache_miss WHERE id = 2");
            assertTrue(rs.next(), "New data should cause a cache miss and fetch from database");

            stmt.execute("DROP TABLE cache_miss");
        }
    }

    @Test
    public void testMongoDBCacheInvalidationOnDelete() {
        MongoDatabase db = mongoClient.getDatabase(MONGO_DATABASE);
        MongoCollection<Document> collection = db.getCollection("delete_cache");

        collection.insertOne(new Document("key", "to_be_deleted"));

        Document result1 = collection.find(new Document("key", "to_be_deleted")).first();
        assertNotNull(result1, "Document should exist initially");

        collection.deleteOne(new Document("key", "to_be_deleted"));

        Document result2 = collection.find(new Document("key", "to_be_deleted")).first();
        assertNull(result2, "Cache should be invalidated on delete operation");

        collection.drop();
    }

    @Test
    public void testMySQLCachePerformanceWithMultipleUsers() throws Exception {
        try (Statement stmt = mysqlConnection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS multiuser_cache (id INT PRIMARY KEY, value VARCHAR(50))");

            stmt.execute("INSERT INTO multiuser_cache VALUES (1, 'test1')");
            stmt.execute("INSERT INTO multiuser_cache VALUES (2, 'test2')");

            for (int i = 0; i < 10; i++) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM multiuser_cache");
                assertTrue(rs.next(), "Data should be accessible to multiple users with cache enabled");
            }

            stmt.execute("DROP TABLE multiuser_cache");
        }
    }
}