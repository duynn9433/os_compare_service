package com.viettel.vtnet.os_compare_service.repo.cassandra;

import com.viettel.vtnet.os_compare_service.entity.cassandra.EntityCassandraRead;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityCassandraReadRepo extends CassandraRepository<EntityCassandraRead, Long> {
    long count();

    @Query("DELETE FROM entitycassandraread WHERE id > :minId")
    void deleteByIdGreaterThan(@Param("minId") Long minId);

    @Query("select * from entitycassandraread where id > :id allow filtering ")
    List<EntityCassandraRead> findByIdGreaterThan(@Param("id") long id);
}
