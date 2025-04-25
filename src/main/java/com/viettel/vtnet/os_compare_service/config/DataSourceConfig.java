package com.viettel.vtnet.os_compare_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "postgresDataSource")
    @ConfigurationProperties("spring.datasource.postgres")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/os_compare")
                .username("postgres_user")
                .password("postgres_password")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "mariaDbDataSource")
    @ConfigurationProperties("spring.datasource.mariadb")
    public DataSource mariaDbDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mariadb://localhost:3306/os_compare")
                .username("mariadb_user")
                .password("mariadb_password")
                .driverClassName("org.mariadb.jdbc.Driver")
                .build();
    }
}