package com.viettel.vtnet.os_compare_service.repo.postgres;

import com.viettel.vtnet.os_compare_service.entity.postgres.Entity2;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Entity2Repo extends JpaRepository<Entity2, Long> {
    long count();
    @Transactional
    void deleteByIdGreaterThan(long id);
}
