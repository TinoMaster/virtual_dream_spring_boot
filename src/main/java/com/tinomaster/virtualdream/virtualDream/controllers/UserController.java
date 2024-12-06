package com.tinomaster.virtualdream.virtualDream.controllers;

import com.tinomaster.virtualdream.virtualDream.dtos.UserDto;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.exceptions.NotFoundException;
import com.tinomaster.virtualdream.virtualDream.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;
	private final ModelMapper mapper;

	private UserDto userToUserDto(User user) {
		return mapper.map(user, UserDto.class);
	}

	private User userDtoToUser(UserDto userDto) {
		return mapper.map(userDto, User.class);
	}

	@GetMapping
	public List<UserDto> getAllUsers() {
		var usersList = StreamSupport.stream(userService.getAllUsers().spliterator(), false).toList();
		return usersList.stream().map(this::userToUserDto).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public UserDto getUserById(Long id) {
		return userToUserDto(userService.getUserById(id));
	}

	@PostMapping
	public UserDto saveUser(@RequestBody UserDto userDto) {
		return userToUserDto(userService.saveUser(userDtoToUser(userDto)));
	}

	@PutMapping("/{id}")
	public void updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
		if (id != userDto.getId()) {
			throw new NotFoundException("User with id " + id + " not found");
		}

		userService.updateUser(id, userDtoToUser(userDto));
	}

	@DeleteMapping("/{id}")
	public void removeUser(@PathVariable Long id) {
		userService.removeUser(id);
	}
}
