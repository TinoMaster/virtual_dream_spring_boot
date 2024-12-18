package com.tinomaster.virtualdream.virtualDream.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tinomaster.virtualdream.virtualDream.entities.Business;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

}
