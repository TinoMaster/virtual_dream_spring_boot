package com.tinomaster.virtualdream.virtualdream.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;

@Repository
public interface BusinessFinalSaleRepository extends JpaRepository<BusinessFinalSale, Long> {

    @Query("SELECT bfs FROM BusinessFinalSale bfs WHERE bfs.business.id = :businessId AND bfs.createdAt BETWEEN :startDate AND :endDate")
    List<BusinessFinalSale> findByBusinessAndDateRange(@Param("businessId") Long businessId,
                                                       @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM business_final_sale_workers bfw " +
            "    WHERE bfw.workers_id = :employeeId " + ")",
            nativeQuery = true)
    boolean existEmployeeByEmployeeId(@Param("employeeId") Long employeeId);


    @Query(value = "SELECT bfs.* " +
            "FROM business_final_sale bfs " +
            "WHERE bfs.business_id = :businessId " +
            "AND bfs.created_at = (SELECT MAX(bfs2.created_at) " +
            "                     FROM business_final_sale bfs2 " +
            "                     WHERE bfs2.business_id = :businessId)",
            nativeQuery = true)
    BusinessFinalSale getLastBusinessFinalSale(@Param("businessId") Long businessId);

    @Query(value = "SELECT DISTINCT bfs.* " +
            "FROM business_final_sale bfs " +
            "JOIN machine m ON m.business_final_sale_id = bfs.id " +
            "WHERE bfs.business_id = :businessId " +
            "  AND DATE(bfs.created_at) <> CURDATE() " +
            "  AND bfs.created_at = ( " +
            "      SELECT MAX(bfs2.created_at) " +
            "      FROM business_final_sale bfs2 " +
            "      JOIN machine m2 ON m2.business_final_sale_id = bfs2.id " +
            "      WHERE m2.id = m.id " +
            "        AND DATE(bfs2.created_at) <> CURDATE() " +
            "        AND bfs2.business_id = :businessId " +
            "  )",
            nativeQuery = true)
    //TODO: Revisar, esta funciones la cree para traer las ventas finales con todas las maquinas
    List<BusinessFinalSale> getLatestBusinessFinalSalesWithAllMachines(@Param("businessId") Long businessId);
}
