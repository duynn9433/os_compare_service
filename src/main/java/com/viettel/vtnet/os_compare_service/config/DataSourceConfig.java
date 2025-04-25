package com.viettel.vtnet.os_compare_service.config;

import com.viettel.vtnet.os_compare_service.service.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Autowired
    private AppConfig appConfig;

    @Primary
    @Bean(name = "postgresDataSource")
    @ConfigurationProperties("spring.datasource.postgres")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create()
                .url(appConfig.getPostgresUrl())
                .username(appConfig.getPostgresUsername())
                .password(appConfig.getPostgresPassword())
                .driverClassName(appConfig.getPostgresDriverClassName())
                .build();
    }

    @Bean(name = "mariaDbDataSource")
    @ConfigurationProperties("spring.datasource.mariadb")
    public DataSource mariaDbDataSource() {
        return DataSourceBuilder.create()
                .url(appConfig.getMariaDbUrl())
                .username(appConfig.getMariaDbUsername())
                .password(appConfig.getPostgresPassword())
                .driverClassName(appConfig.getMariaDbDriverClassName())
                .build();
    }
}