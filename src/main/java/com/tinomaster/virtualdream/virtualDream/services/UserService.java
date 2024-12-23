package com.tinomaster.virtualdream.virtualDream.services;

import com.tinomaster.virtualdream.virtualDream.dtos.UserDto;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final ModelMapper mapper;

	private final UserRepository userRepository;

	private final BusinessService businessService;

	private User findOrThrow(final long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User by id " + id + " not found"));
	}

	@Transactional
	public User saveUser(UserDto userDto) {
		User user = mapper.map(userDto, User.class);

		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public List<User> getUnauthorizedUsers() {
		return userRepository.findByActiveFalse();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User by id " + id + " not found"));
	}

	public void activeUser(Long id) {
		userRepository.activeUser(id);
	}

	@Transactional
	public void denyUser(Long id) {
		businessService.deleteBusinessesByUserId(id);
		userRepository.deleteById(id);
	}

	public void removeUser(Long id) {
		userRepository.deleteById(id);
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void updateUser(Long id, User user) {
		findOrThrow(id);
		userRepository.save(user);
	}
}
