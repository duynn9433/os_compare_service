package com.viettel.vtnet.os_compare_service.controller;

import com.viettel.vtnet.os_compare_service.entity.postgres.Entity1;
import com.viettel.vtnet.os_compare_service.entity.postgres.Entity2;
import com.viettel.vtnet.os_compare_service.entity.postgres.Entity43;
import com.viettel.vtnet.os_compare_service.service.database.mariadb.Entity1MariaDbBufferedWriter;
import com.viettel.vtnet.os_compare_service.service.database.mariadb.Entity2MariaDbBufferedWriter;
import com.viettel.vtnet.os_compare_service.service.database.postgres.Entity1BufferedWriter;
import com.viettel.vtnet.os_compare_service.service.database.postgres.Entity2BufferedWriter;
import com.viettel.vtnet.os_compare_service.service.database.GetDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rdb")
public class RelationalDatabaseController {
    private final GetDataService getDataService;
    private final Entity2BufferedWriter entity2BufferedDbWriter;
    private final Entity1BufferedWriter entity1BufferedDbWriter;
    private final Entity2MariaDbBufferedWriter entity2MariaDbBufferedWriter;
    private final Entity1MariaDbBufferedWriter entity1MariaDbBufferedWriter;


    public RelationalDatabaseController(GetDataService getDataService,
                                        Entity2BufferedWriter entity2BufferedWriter,
                                        Entity1BufferedWriter entity1BufferedDbWriter, Entity2MariaDbBufferedWriter entity2MariaDbBufferedWriter, Entity1MariaDbBufferedWriter entity1MariaDbBufferedWriter) {
        this.entity2BufferedDbWriter = entity2BufferedWriter;
        this.getDataService = getDataService;
        this.entity1BufferedDbWriter = entity1BufferedDbWriter;
        this.entity2MariaDbBufferedWriter = entity2MariaDbBufferedWriter;
        this.entity1MariaDbBufferedWriter = entity1MariaDbBufferedWriter;
    }

    /**RD01 - postgres*/
    @GetMapping("/read/{id}")
    public ResponseEntity<?> readDatabase(@PathVariable("id") Long id) {
        if (id == 0) {
            return ResponseEntity.ok(getDataService.getEntity1ReadCount());
        }
        Entity1 entity1 = getDataService.getEntity1ById(id);
        if (entity1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity1);
    }

    /**RD01 - mariadb*/
    @GetMapping("/read-mariadb/{id}")
    public ResponseEntity<?> readMariaDbDatabase(@PathVariable("id") Long id) {
        if (id == 0) {
            return ResponseEntity.ok(getDataService.getEntity1MariaDbReadCount());
        }
        com.viettel.vtnet.os_compare_service.entity.mariadb.Entity1 entity1 = getDataService.getEntity1MariaDbById(id);
        if (entity1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity1);
    }

    @GetMapping("/write")
    public ResponseEntity<?> writeDatabase() {
        try {
            entity2BufferedDbWriter.add(Entity2.generateRandom());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
    @GetMapping("/write-mariadb")
    public ResponseEntity<?> writeMariaDbDatabase() {
        try {
            entity2MariaDbBufferedWriter.add(com.viettel.vtnet.os_compare_service.entity.mariadb.Entity2.generateRandom());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/write/clean")
    public ResponseEntity<?> cleanDatabase() {
        try {
            entity2BufferedDbWriter.cleanDatabase();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/write-mariadb/clean")
    public ResponseEntity<?> cleanMariaDbDatabase() {
        try {
            entity2MariaDbBufferedWriter.cleanDatabase();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/write/count")
    public ResponseEntity<?> countDatabase() {
        try {
            return ResponseEntity.ok(entity2BufferedDbWriter.countDatabase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/write-mariadb/count")
    public ResponseEntity<?> countMariaDbDatabase() {
        try {
            return ResponseEntity.ok(entity2MariaDbBufferedWriter.countDatabase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/write/update")
    public ResponseEntity<?> updateDatabase(@RequestBody Entity1 entity1) {
        try {
            entity1BufferedDbWriter.add(entity1);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/write-mariadb/update")
    public ResponseEntity<?> updateMariaDbDatabase(@RequestBody com.viettel.vtnet.os_compare_service.entity.mariadb.Entity1 entity1) {
        try {
            entity1MariaDbBufferedWriter.add(entity1);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read-multi/{id}")
    public ResponseEntity<?> readMultiDatabase(@PathVariable("id") Long id) {
        if (id == 0) {
            return ResponseEntity.ok(getDataService.getEntity43ReadCount());
        }
        Entity43 entity43 = getDataService.getEntity43ById(id);
        if (entity43 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity43);
    }

    @GetMapping("/read-multi-mariadb/{id}")
    public ResponseEntity<?> readMultiMariaDbDatabase(@PathVariable("id") Long id) {
        if (id == 0) {
            return ResponseEntity.ok(getDataService.getEntity43MariaDbById(id));
        }
        com.viettel.vtnet.os_compare_service.entity.mariadb.Entity43 entity43 = getDataService.getEntity43MariaDbById(id);
        if (entity43 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity43);
    }
}
