package com.tinomaster.virtualdream.virtualDream.repositories;

import com.tinomaster.virtualdream.virtualDream.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}