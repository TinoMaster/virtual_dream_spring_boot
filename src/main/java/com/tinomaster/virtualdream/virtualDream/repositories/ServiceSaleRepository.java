package com.tinomaster.virtualdream.virtualDream.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualDream.entities.ServiceSale;

@Repository
public interface ServiceSaleRepository extends JpaRepository<ServiceSale, Long> {

}
