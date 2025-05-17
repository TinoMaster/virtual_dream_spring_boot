package com.tinomaster.virtualdream.virtualdream.repositories;

import com.tinomaster.virtualdream.virtualdream.entities.ConsumableKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumableKeyRepository extends JpaRepository<ConsumableKey, Long> {
}
