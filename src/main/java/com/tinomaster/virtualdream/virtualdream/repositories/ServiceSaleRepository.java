package com.tinomaster.virtualdream.virtualdream.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.ServiceSale;

@Repository
public interface ServiceSaleRepository extends JpaRepository<ServiceSale, Long> {

    @Query("SELECT bfs FROM ServiceSale bfs WHERE bfs.business.id = :businessId AND bfs.createdAt BETWEEN :startDate AND :endDate")
    List<ServiceSale> findByBusinessAndDateRange(@Param("businessId") Long businessId,
                                                 @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<ServiceSale> findByBusinessFinalSaleIsNullAndCreatedAtBefore(LocalDateTime dateTime);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM service_sale WHERE service_id = :serviceId)", nativeQuery = true)
    public boolean existServiceByServiceId(@Param("serviceId") Long serviceId);
}
