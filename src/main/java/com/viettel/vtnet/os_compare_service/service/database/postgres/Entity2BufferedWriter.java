package com.viettel.vtnet.os_compare_service.service.database.postgres;

import com.viettel.vtnet.os_compare_service.entity.postgres.Entity2;
import com.viettel.vtnet.os_compare_service.repo.postgres.Entity2Repo;
import com.viettel.vtnet.os_compare_service.service.database.BufferedDbWriter;
import org.springframework.stereotype.Service;

@Service
public class Entity2BufferedWriter extends BufferedDbWriter<Entity2> {
    public Entity2BufferedWriter(Entity2Repo repo) {
        super(repo);
    }
}