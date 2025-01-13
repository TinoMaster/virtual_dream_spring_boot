package com.tinomaster.virtualdream.virtualdream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.Consumable;

@Repository
public interface ConsumableRepository extends JpaRepository<Consumable, Long> {

	@Query(value = "SELECT * FROM consumable WHERE business_id = :businessId", nativeQuery = true)
	List<Consumable> findByBusinessId(@Param("businessId") Long businessId);
}
