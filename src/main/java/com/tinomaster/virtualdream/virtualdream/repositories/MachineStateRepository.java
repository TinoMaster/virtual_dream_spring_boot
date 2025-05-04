package com.tinomaster.virtualdream.virtualdream.repositories;

import com.tinomaster.virtualdream.virtualdream.entities.MachineState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MachineStateRepository extends JpaRepository<MachineState, Long> {

    // 1. Buscar por ID de BusinessFinalSale
    List<MachineState> findByBusinessFinalSaleId(Long businessFinalSaleId);

    // 2. Buscar por rango de fechas (LocalDateTime)
    List<MachineState> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 3. Buscar por ID de BusinessFinalSale Y IDs de Machine específicas
    List<MachineState> findByBusinessFinalSaleIdAndMachineIdIn(Long businessFinalSaleId, List<Long> machineIds);

    // 4. Buscar por ID de Machine y rango de fechas
    List<MachineState> findByMachineIdAndDateBetween(Long machineId, LocalDateTime startDate, LocalDateTime endDate);

    // EXTRA: Buscar por una fecha específica (LocalDate)
    // Convierte LocalDate a un rango LocalDateTime (inicio y fin del día)
    @Query("SELECT ms FROM MachineState ms WHERE ms.date >= :startOfDay AND ms.date < :endOfDay")
    List<MachineState> findByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    // Podrías añadir una sobrecarga en tu Service para usar esta con LocalDate:
    // default List<MachineState> findByDate(LocalDate date) {
    //     LocalDateTime startOfDay = date.atStartOfDay();
    //     LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
    //     return findByDate(startOfDay, endOfDay);
    // }

    // EXTRA 2: Buscar por fecha específica Y Business ID (a través de BusinessFinalSale)
    @Query("SELECT ms FROM MachineState ms JOIN ms.businessFinalSale bfs WHERE bfs.business.id = :businessId AND ms.date >= :startOfDay AND ms.date < :endOfDay")
    List<MachineState> findByBusinessIdAndDate(@Param("businessId") Long businessId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    // EXTRA 3: Buscar el último estado de CADA máquina para un Business ID antes o en una fecha específica
    @Query("SELECT ms FROM MachineState ms JOIN ms.businessFinalSale bfs WHERE bfs.business.id = :businessId AND ms.date <= :targetDate AND ms.date = (SELECT MAX(subMs.date) FROM MachineState subMs JOIN subMs.businessFinalSale subBfs WHERE subMs.machine.id = ms.machine.id AND subBfs.business.id = :businessId AND subMs.date <= :targetDate)")
    List<MachineState> findLatestStatesByBusinessIdBeforeDate(@Param("businessId") Long businessId, @Param("targetDate") LocalDateTime targetDate);

}
