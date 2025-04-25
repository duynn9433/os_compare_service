package com.viettel.vtnet.os_compare_service.repo.mariadb;

import com.viettel.vtnet.os_compare_service.entity.mariadb.Entity2;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Entity2MariaDbRepo extends JpaRepository<Entity2, Long> {
    long count();
    @Transactional
    void deleteByIdGreaterThan(long id);
}
