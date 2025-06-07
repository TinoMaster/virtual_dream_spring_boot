package com.tinomaster.virtualdream.virtualdream.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.Debt;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    /**
     * Obtiene el total no pagado de deudas para un negocio en un rango de fechas específico
     *
     * @param businessId ID del negocio
     * @param startDate  Fecha de inicio
     * @param endDate    Fecha de fin
     * @return Total no pagado (total - paid) de todas las deudas en el rango
     */
    @Query(value = "SELECT COALESCE(SUM(d.total - d.paid), 0) " +
            "FROM debt d " +
            "JOIN business_final_sale bfs ON d.business_final_sale_id = bfs.id " +
            "WHERE bfs.business_id = :businessId " +
            "AND bfs.created_at BETWEEN CAST(:startDate AS TIMESTAMP) AND CAST(:endDate AS TIMESTAMP)",
            nativeQuery = true)
    Float getTotalUnpaidDebtsByBusinessAndDateRange(
            @Param("businessId") Long businessId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene todas las deudas pendientes de un negocio
     *
     * @param businessId ID del negocio
     * @return Todas las deudas pendientes del negocio
     */
    @Query(value = "SELECT * FROM debt WHERE business_id = :businessId AND d.paid < d.total", nativeQuery = true)
    List<Debt> findPendingDebtsByBusinessId(@Param("businessId") Long businessId);

    /**
     * Obtiene todas las deudas de un negocio
     *
     * @param businessId ID del negocio
     * @return Todas las deudas del negocio
     */
    @Query(value = "SELECT * FROM debt WHERE business_id = :businessId", nativeQuery = true)
    List<Debt> findDebtsByBusinessId(@Param("businessId") Long businessId);
}

