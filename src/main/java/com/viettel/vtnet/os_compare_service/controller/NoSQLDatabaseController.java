package com.viettel.vtnet.os_compare_service.controller;

import com.viettel.vtnet.os_compare_service.entity.cassandra.EntityCassandraWrite;
import com.viettel.vtnet.os_compare_service.entity.mongo.EntityMongoWrite;
import com.viettel.vtnet.os_compare_service.service.database.cassandra.EntityCassandraWriteService;
import com.viettel.vtnet.os_compare_service.service.database.mongo.EntityMongoWriteService;
import com.viettel.vtnet.os_compare_service.service.database.GetDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nosql")
public class NoSQLDatabaseController {
    private final GetDataService getDataService;
    private final EntityMongoWriteService entityMongoWriteService;
    private final EntityCassandraWriteService entityCassandraWriteService;

    public NoSQLDatabaseController(GetDataService getDataService, EntityMongoWriteService entityMongoWriteService, EntityCassandraWriteService entityCassandraWriteService) {
        this.getDataService = getDataService;
        this.entityMongoWriteService = entityMongoWriteService;
        this.entityCassandraWriteService = entityCassandraWriteService;
    }

    @GetMapping("/cassandra")
    public ResponseEntity<?> getCassandraData(@RequestParam Long minId) {
        // Implement your logic to fetch data from Cassandra
        return ResponseEntity.ok(getDataService.getEntityCassandraReadByMinId(minId));
    }

    @GetMapping("/mongo")
    public ResponseEntity<?> getMongoData(@RequestParam Long minId) {
        // Implement your logic to fetch data from Cassandra
        return ResponseEntity.ok(getDataService.getEntityMongoReadByMinId(minId));
    }

    @PostMapping("/mongo")
    public ResponseEntity<?> postMongoData(@RequestBody List<EntityMongoWrite> entityMongoWrites) {
        entityMongoWriteService.cleanDatabase();
        entityMongoWriteService.addAll(entityMongoWrites);
        return ResponseEntity.ok(entityMongoWriteService.countDatabase());
    }

    @PostMapping("/cassandra")
    public ResponseEntity<?> postCassandraData(@RequestBody List<EntityCassandraWrite> entityCassandraWrites) {
        entityCassandraWriteService.cleanDatabase();
        entityCassandraWriteService.addAll(entityCassandraWrites);
        return ResponseEntity.ok(entityCassandraWriteService.countDatabase());
    }
}
