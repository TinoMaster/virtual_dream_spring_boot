package com.tinomaster.virtualdream.virtualdream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.ServiceEntity;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    @Query(value = "SELECT * FROM service WHERE business_id = :businessId AND finished_at IS NULL", nativeQuery = true)
    List<ServiceEntity> findLastByBusinessId(@Param("businessId") Long businessId);

    @Query(value = "SELECT * FROM service WHERE service_key_id = :serviceKeyId AND finished_at IS NOT NULL", nativeQuery = true)
    ServiceEntity findLastByServiceKeyId(@Param("serviceKeyId") Long serviceKeyId);

    @Query(value = "SELECT * FROM service WHERE service_key_id = :serviceKeyId", nativeQuery = true)
    List<ServiceEntity> findAllByServiceKeyId(@Param("serviceKeyId") Long serviceKeyId);
}
