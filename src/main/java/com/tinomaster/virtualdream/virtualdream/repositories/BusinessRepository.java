package com.tinomaster.virtualdream.virtualdream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tinomaster.virtualdream.virtualdream.entities.Business;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

	@Query(value = "SELECT * FROM business WHERE owner_id = :id", nativeQuery = true)
	List<Business> findBusinessesByOwnerId(@Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM business WHERE owner_id = :id", nativeQuery = true)
	void deleteBusinessesByUserId(@Param("id") Long id);
}
