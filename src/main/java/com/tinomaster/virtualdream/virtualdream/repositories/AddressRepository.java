package com.tinomaster.virtualdream.virtualdream.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualdream.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
