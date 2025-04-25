package com.viettel.vtnet.os_compare_service.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AppConfig {
    @Value("${file.lineCount}")
    private long lineCount;

    @Value("${app.postgres.read}")
    private long postgresRead;
    @Value("${app.postgres.read.relation}")
    private long postgresReadRelation;
    @Value("${app.mongo.read}")
    private long mongoRead;
    @Value("${app.mongo.write}")
    private long mongoWrite;
    @Value("${app.cassandra.read}")
    private long cassandraRead;
    @Value("${app.cassandra.write}")
    private long cassandraWrite;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Value("${spring.datasource.postgres.url}")
    private String postgresUrl;
    @Value("${spring.datasource.postgres.username}")
    private String postgresUsername;
    @Value("${spring.datasource.postgres.password}")
    private String postgresPassword;
    @Value("${spring.datasource.postgres.driver-class-name}")
    private String postgresDriverClassName;



    @Value("${spring.datasource.mariadb.url}")
    private String mariaDbUrl;
    @Value("${spring.datasource.mariadb.username}")
    private String mariaDbUsername;
    @Value("${spring.datasource.mariadb.password}")
    private String mariaDbPassword;
    @Value("${spring.datasource.mariadb.driver-class-name}")
    private String mariaDbDriverClassName;

}
