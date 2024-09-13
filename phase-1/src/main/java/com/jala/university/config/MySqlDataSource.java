package com.jala.university.config;


import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//Provides a customized data configuration to database
@Configuration
public class MySqlDataSource {
    @ConfigurationProperties("spring.datasource.mysql")
    @Bean
    public DataSourceProperties mySqlDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    DataSource mysqlDatasource(){
        return mySqlDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
