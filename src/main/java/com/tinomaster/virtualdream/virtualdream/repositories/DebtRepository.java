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

    /**
     * Obtiene todas las ventas en un rango de fechas para un negocio
     *
     * @param businessId ID del negocio
     * @param startDate  Fecha de inicio
     * @param endDate    Fecha de fin
     * @return Todas las ventas del negocio en el rango
     */
    @Query(value = "SELECT * FROM business_final_sale WHERE business_id = :businessId AND created_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Debt> findSalesByBusinessAndDateRange(@Param("businessId") Long businessId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

