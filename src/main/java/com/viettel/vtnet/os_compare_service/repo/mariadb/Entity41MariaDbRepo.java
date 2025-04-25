package com.viettel.vtnet.os_compare_service.repo.mariadb;

import com.viettel.vtnet.os_compare_service.entity.mariadb.Entity41;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Entity41MariaDbRepo extends JpaRepository<Entity41, Long> {
    long count();
    @Transactional
    void deleteByIdGreaterThan(long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Entity41 e WHERE e.id > :maxID")
    void deleteByMaxId(@Param("maxID") long maxID);
}
