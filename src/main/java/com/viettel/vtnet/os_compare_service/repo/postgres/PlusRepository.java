package com.viettel.vtnet.os_compare_service.repo.postgres;

import com.viettel.vtnet.os_compare_service.entity.postgres.PlusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlusRepository extends JpaRepository<PlusEntity, Long> {

    long count();

    void deleteByIdGreaterThan(long id);

}
