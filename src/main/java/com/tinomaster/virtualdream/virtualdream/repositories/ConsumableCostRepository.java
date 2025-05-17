package com.tinomaster.virtualdream.virtualdream.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.ConsumableCost;

@Repository
public interface ConsumableCostRepository extends JpaRepository<ConsumableCost, Long> {
    //Query para buscar si existe algun campo en la columna consumable_id con el valor del parametro consumableId, debe ser con un exist para que me devuelba un booleano
    @Query(value = "SELECT EXISTS (SELECT * FROM consumable_cost WHERE consumable_id = :consumableId)", nativeQuery = true)
    boolean existsCostByConsumableId(@Param("consumableId") Long consumableId);

    @Query(value = "SELECT EXISTS (SELECT * FROM consumable_cost WHERE service_id = :serviceId)", nativeQuery = true)
    boolean existsCostByServiceId(@Param("serviceId") Long serviceId);
}
