package com.tinomaster.virtualdream.virtualdream.repositories;

import com.tinomaster.virtualdream.virtualdream.entities.Task;
import com.tinomaster.virtualdream.virtualdream.enums.ENoteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByBusinessId(Long businessId, Pageable pageable);

    Page<Task> findAllByBusinessIdAndStatus(Long businessId, ENoteStatus status, Pageable pageable);
}
