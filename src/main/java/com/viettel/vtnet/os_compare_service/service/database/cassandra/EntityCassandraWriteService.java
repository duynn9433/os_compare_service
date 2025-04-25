package com.viettel.vtnet.os_compare_service.service.database.cassandra;

import com.viettel.vtnet.os_compare_service.entity.cassandra.EntityCassandraWrite;
import com.viettel.vtnet.os_compare_service.repo.cassandra.EntityCassandraWriteRepo;
import com.viettel.vtnet.os_compare_service.service.database.BufferedDbWriter;
import org.springframework.stereotype.Service;

@Service
public class EntityCassandraWriteService extends BufferedDbWriter<EntityCassandraWrite> {
    public EntityCassandraWriteService(EntityCassandraWriteRepo repo) {
        super(repo);
    }
}
