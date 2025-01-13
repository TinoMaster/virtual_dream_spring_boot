package com.tinomaster.virtualdream.virtualdream.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.ConsumableCost;

@Repository
public interface ConsumableCostRepository extends JpaRepository<ConsumableCost, Long> {

}
