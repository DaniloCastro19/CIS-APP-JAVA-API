package com.jala.university.api.config;

import com.jala.university.config.MySqlDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MySqlDataSourceTest {

    @InjectMocks
    private MySqlDataSource mySqlDataSource;

    @Test
    void shouldCreateDataSourceProperties() {
        DataSourceProperties properties = mySqlDataSource.mySqlDataSourceProperties();

        assertNotNull(properties, "DataSourceProperties should not be null");
    }
}