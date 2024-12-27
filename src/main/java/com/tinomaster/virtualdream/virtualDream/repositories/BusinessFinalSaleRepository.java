package com.tinomaster.virtualdream.virtualDream.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualDream.entities.BusinessFinalSale;

@Repository
public interface BusinessFinalSaleRepository extends JpaRepository<BusinessFinalSale, Long> {
	
	@Query("SELECT bfs FROM BusinessFinalSale bfs WHERE bfs.business.id = :businessId AND bfs.createdAt BETWEEN :startDate AND :endDate")
	List<BusinessFinalSale> findByBusinessAndDateRange(@Param("businessId") Long businessId,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
