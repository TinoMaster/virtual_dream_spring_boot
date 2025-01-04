package com.tinomaster.virtualdream.virtualDream.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualDream.entities.Consumable;

@Repository
public interface ConsumableRepository extends JpaRepository<Consumable, Long> {

}
