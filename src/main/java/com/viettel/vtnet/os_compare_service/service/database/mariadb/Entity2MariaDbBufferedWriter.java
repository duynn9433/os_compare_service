package com.viettel.vtnet.os_compare_service.service.database.mariadb;

import com.viettel.vtnet.os_compare_service.entity.mariadb.Entity2;
import com.viettel.vtnet.os_compare_service.repo.mariadb.Entity2MariaDbRepo;
import com.viettel.vtnet.os_compare_service.service.database.BufferedDbWriter;
import org.springframework.stereotype.Service;

@Service
public class Entity2MariaDbBufferedWriter extends BufferedDbWriter<Entity2> {
    public Entity2MariaDbBufferedWriter(Entity2MariaDbRepo repo) {
        super(repo);
    }
}