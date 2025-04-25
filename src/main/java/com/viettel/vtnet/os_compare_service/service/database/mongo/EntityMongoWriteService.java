package com.viettel.vtnet.os_compare_service.service.database.mongo;

import com.viettel.vtnet.os_compare_service.entity.mongo.EntityMongoWrite;
import com.viettel.vtnet.os_compare_service.repo.mongo.EntityMongoWriteRepo;
import com.viettel.vtnet.os_compare_service.service.database.BufferedDbWriter;
import org.springframework.stereotype.Service;

@Service
public class EntityMongoWriteService extends BufferedDbWriter<EntityMongoWrite> {
    public EntityMongoWriteService(EntityMongoWriteRepo repo) {
        super(repo);
    }
}
