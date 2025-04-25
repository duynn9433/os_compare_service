package com.viettel.vtnet.os_compare_service.repo.mongo;

import com.viettel.vtnet.os_compare_service.entity.mongo.EntityMongoRead;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityMongoReadRepo extends MongoRepository<EntityMongoRead, String> {

    long count();
    @Transactional
    void deleteByIdGreaterThan(long id);

    List<EntityMongoRead> findAllByIdGreaterThanOrderByIdDesc(long minId);
}
