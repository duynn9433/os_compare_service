package com.viettel.vtnet.os_compare_service.service.database;

import com.viettel.vtnet.os_compare_service.entity.postgres.Entity1;
import com.viettel.vtnet.os_compare_service.entity.postgres.Entity43;
import com.viettel.vtnet.os_compare_service.entity.cassandra.EntityCassandraRead;
import com.viettel.vtnet.os_compare_service.entity.mongo.EntityMongoRead;
import com.viettel.vtnet.os_compare_service.repo.postgres.Entity1Repo;
import com.viettel.vtnet.os_compare_service.repo.postgres.Entity43Repo;
import com.viettel.vtnet.os_compare_service.repo.cassandra.EntityCassandraReadRepo;
import com.viettel.vtnet.os_compare_service.repo.mongo.EntityMongoReadRepo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GetDataService {
    //postgres
    private final Entity1Repo entity1Repo;
    private final Entity43Repo entity43Repo;
    //mariadb
    private final com.viettel.vtnet.os_compare_service.repo.mariadb.Entity1MariaDbRepo entity1MariaDbRepo;
    private final com.viettel.vtnet.os_compare_service.repo.mariadb.Entity43MariaDbRepo entity43MariaDbRepo;

    private final EntityCassandraReadRepo entityCassandraReadRepo;
    private final EntityMongoReadRepo entityMongoReadRepo;

    public GetDataService(Entity1Repo entity1Repo, Entity43Repo entity43Repo, com.viettel.vtnet.os_compare_service.repo.mariadb.Entity1MariaDbRepo entity1MariaDbRepo, com.viettel.vtnet.os_compare_service.repo.mariadb.Entity43MariaDbRepo entity43MariaDbRepo, EntityCassandraReadRepo entityCassandraReadRepo, EntityMongoReadRepo entityMongoReadRepo) {
        this.entity1Repo = entity1Repo;
        this.entity43Repo = entity43Repo;
        this.entity1MariaDbRepo = entity1MariaDbRepo;
        this.entity43MariaDbRepo = entity43MariaDbRepo;
        this.entityCassandraReadRepo = entityCassandraReadRepo;
        this.entityMongoReadRepo = entityMongoReadRepo;
    }

    public Entity1 getEntity1ById(long id) {
        return entity1Repo.findById(id).orElse(null);
    }

    public com.viettel.vtnet.os_compare_service.entity.mariadb.Entity1 getEntity1MariaDbById(long id) {
        return entity1MariaDbRepo.findById(id).orElse(null);
    }

    public long getEntity1ReadCount() {
        return entity1Repo.count();
    }

    public long getEntity43ReadCount() {
        return entity43Repo.count();
    }

    public long getEntity1MariaDbReadCount() {
        return entity1MariaDbRepo.count();
    }

    public long getEntity43MariaDbReadCount() {
        return entity43MariaDbRepo.count();
    }

    public Entity43 getEntity43ById(long id) {
        return entity43Repo.findById(id).orElse(null);
    }

    public com.viettel.vtnet.os_compare_service.entity.mariadb.Entity43 getEntity43MariaDbById(long id) {
        return entity43MariaDbRepo.findById(id).orElse(null);
    }

    public List<EntityCassandraRead> getEntityCassandraReadByMinId(long minId) {
        return entityCassandraReadRepo.findByIdGreaterThan(minId)
                .stream()
                .sorted(Comparator.comparing(EntityCassandraRead::getId))
                .toList();
    }

    public List<EntityMongoRead> getEntityMongoReadByMinId(long minId) {
        return entityMongoReadRepo.findAllByIdGreaterThanOrderByIdDesc(minId);
    }
}
