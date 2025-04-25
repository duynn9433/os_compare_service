package com.viettel.vtnet.os_compare_service.service.database.mariadb;

import com.viettel.vtnet.os_compare_service.entity.mariadb.Entity1;
import com.viettel.vtnet.os_compare_service.repo.mariadb.Entity1MariaDbRepo;
import com.viettel.vtnet.os_compare_service.service.database.BufferedDbWriter;
import org.springframework.stereotype.Service;

@Service
public class Entity1MariaDbBufferedWriter extends BufferedDbWriter<Entity1> {
    public Entity1MariaDbBufferedWriter(Entity1MariaDbRepo repo) {
        super(repo);
    }
}
