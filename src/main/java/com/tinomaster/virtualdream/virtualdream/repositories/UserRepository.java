package com.tinomaster.virtualdream.virtualdream.repositories;

import com.tinomaster.virtualdream.virtualdream.entities.User;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByActiveFalse();

    // Query para verificar si existe algun usuario que tenga el role de SUPERADMIN
    @Query(value = "SELECT * FROM users WHERE role = 'ROLE_SUPERADMIN' LIMIT 1", nativeQuery = true)
    User findSuperAdmin();

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET active = true WHERE id = :id", nativeQuery = true)
    void activeUser(@Param("id") Long id);
}