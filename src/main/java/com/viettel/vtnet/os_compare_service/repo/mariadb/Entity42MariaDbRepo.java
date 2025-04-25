package com.viettel.vtnet.os_compare_service.repo.mariadb;

import com.viettel.vtnet.os_compare_service.entity.mariadb.Entity42;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Entity42MariaDbRepo extends JpaRepository<Entity42, Long> {
    long count();
    @Transactional
    void deleteByIdGreaterThan(long id);
    @Modifying
    @Transactional
    @Query("DELETE FROM Entity42 e WHERE e.id > :maxID")
    void deleteByMaxId(@Param("maxID") long maxID);
}
