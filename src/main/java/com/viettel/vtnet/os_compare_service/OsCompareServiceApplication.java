package com.viettel.vtnet.os_compare_service;

import com.viettel.vtnet.os_compare_service.entity.cassandra.EntityCassandraRead;
import com.viettel.vtnet.os_compare_service.entity.mongo.EntityMongoRead;
import com.viettel.vtnet.os_compare_service.entity.postgres.Entity1;
import com.viettel.vtnet.os_compare_service.entity.postgres.Entity43;
import com.viettel.vtnet.os_compare_service.entity.postgres.PlusEntity;
import com.viettel.vtnet.os_compare_service.repo.cassandra.EntityCassandraReadRepo;
import com.viettel.vtnet.os_compare_service.repo.mariadb.Entity1MariaDbRepo;
import com.viettel.vtnet.os_compare_service.repo.mariadb.Entity41MariaDbRepo;
import com.viettel.vtnet.os_compare_service.repo.mariadb.Entity43MariaDbRepo;
import com.viettel.vtnet.os_compare_service.repo.mongo.EntityMongoReadRepo;
import com.viettel.vtnet.os_compare_service.repo.postgres.Entity1Repo;
import com.viettel.vtnet.os_compare_service.repo.postgres.Entity41Repo;
import com.viettel.vtnet.os_compare_service.repo.postgres.Entity43Repo;
import com.viettel.vtnet.os_compare_service.repo.postgres.PlusRepository;
import com.viettel.vtnet.os_compare_service.service.AppConfig;
import com.viettel.vtnet.os_compare_service.service.FileService;
import com.viettel.vtnet.os_compare_service.service.database.GenerateDataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class OsCompareServiceApplication implements CommandLineRunner {
    @Autowired
    private FileService fileWriteService;
    @Autowired
    private GenerateDataService generateDataService;
    @Autowired
    private AppConfig appConfig;
    //Postgres
    @Autowired
    private Entity1Repo entity1Repo;
    @Autowired
    private Entity43Repo entity43Repo;
    @Autowired
    private Entity41Repo entity41Repo;
    //MariaDb
    @Autowired
    private Entity1MariaDbRepo entity1MariaDbRepo;
    @Autowired
    private Entity43MariaDbRepo entity43MariaDbRepo;
    @Autowired
    private Entity41MariaDbRepo entity41MariaDbRepo;
    //Mongo
    @Autowired
    private EntityMongoReadRepo entityMongoReadRepo;
    //Cassandra
    @Autowired
    private EntityCassandraReadRepo entityCassandraReadRepo;

    @Autowired
    private PlusRepository plusRepo;

    public static void main(String[] args) {
        SpringApplication.run(OsCompareServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Khi ứng dụng bắt đầu, tạo file input.txt với số dòng được cấu hình
        log.info("Khởi tạo file input.txt với số dòng cấu hình...");
        String result = fileWriteService.writeFile("input.txt");
        log.info(result); // In ra kết quả ghi file

        generateDataService.generateWhenStartParallel(Entity1.class,
                entity1Repo,entity1Repo, appConfig.getPostgresRead(),20
        );

        generateDataService.generateWhenStartParallel(com.viettel.vtnet.os_compare_service.entity.mariadb.Entity1.class,
                entity1MariaDbRepo,entity1MariaDbRepo, appConfig.getPostgresRead(),20
        );

        generateDataService.generateWhenStartParallel(Entity43.class,
                entity43Repo, entity41Repo, appConfig.getPostgresReadRelation(),20
        );

        generateDataService.generateWhenStartParallel(com.viettel.vtnet.os_compare_service.entity.mariadb.Entity43.class,
                entity43MariaDbRepo, entity41MariaDbRepo, appConfig.getPostgresReadRelation(),20
        );

        generateDataService.generateWhenStartParallel(EntityMongoRead.class,
                entityMongoReadRepo, entityMongoReadRepo, appConfig.getMongoRead(),20
        );

        generateDataService.generateWhenStartParallel(PlusEntity.class,
                plusRepo, plusRepo, 10000,20
        );

        generateDataService.generateWhenStartParallel(EntityCassandraRead.class,
                entityCassandraReadRepo, entityCassandraReadRepo, appConfig.getCassandraRead(),20);
    }
}
