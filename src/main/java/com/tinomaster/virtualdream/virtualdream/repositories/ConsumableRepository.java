package com.tinomaster.virtualdream.virtualdream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.Consumable;

@Repository
public interface ConsumableRepository extends JpaRepository<Consumable, Long> {

    @Query(value = "SELECT * FROM consumable WHERE business_id = :businessId AND finished_at IS NULL", nativeQuery = true)
    List<Consumable> findLastByBusinessId(@Param("businessId") Long businessId);

    //Query para buscar el consumable que el campo consumableKey sea igual al parametro consumableKey y finishedAt sea null
    @Query(value = "SELECT * FROM consumable WHERE consumable_key_id = :consumableKeyId AND finished_at IS NULL", nativeQuery = true)
    Consumable findLastByConsumableKey(@Param("consumableKeyId") Long consumableKey);

    //Query para traer todos los consumables que consumableKey sea igual al parametro consumableKey
    @Query(value = "SELECT * FROM consumable WHERE consumable_key_id = :consumableKeyId", nativeQuery = true)
    List<Consumable> findAllByConsumableKey(@Param("consumableKeyId") Long consumableKey);
}
