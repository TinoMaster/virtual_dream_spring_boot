package com.tinomaster.virtualdream.virtualDream.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tinomaster.virtualdream.virtualDream.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query(value = "SELECT * FROM employees WHERE user_id IN (:userIds)", nativeQuery = true)
	List<Employee> findByUserIds(@Param("userIds") List<Long> userIds);

	@Query(value = "SELECT * FROM employees WHERE user_id = :userId", nativeQuery = true)
	Employee findByUserId(@Param("userId") Long userId);
}
