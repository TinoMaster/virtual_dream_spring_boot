package com.tinomaster.virtualdream.virtualDream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualDream.entities.ServiceEntity;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
	@Query(value = "SELECT * FROM service WHERE business_id = :businessId", nativeQuery = true)
	List<ServiceEntity> findByBusinessId(@Param("businessId") Long businessId);
}
