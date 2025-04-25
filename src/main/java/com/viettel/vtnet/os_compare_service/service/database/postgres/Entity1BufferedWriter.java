package com.viettel.vtnet.os_compare_service.service.database.postgres;

import com.viettel.vtnet.os_compare_service.entity.postgres.Entity1;
import com.viettel.vtnet.os_compare_service.repo.postgres.Entity1Repo;
import com.viettel.vtnet.os_compare_service.service.database.BufferedDbWriter;
import org.springframework.stereotype.Service;

@Service
public class Entity1BufferedWriter extends BufferedDbWriter<Entity1> {
    public Entity1BufferedWriter(Entity1Repo repo) {
        super(repo);
    }
}
