package com.tinomaster.virtualdream.virtualdream.repositories;

import com.tinomaster.virtualdream.virtualdream.entities.ServiceKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceKeyRepository extends JpaRepository<ServiceKey, Long> {
}
