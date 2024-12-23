package com.tinomaster.virtualdream.virtualDream.controllers;

import com.tinomaster.virtualdream.virtualDream.dtos.UserDto;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.exceptions.NotFoundException;
import com.tinomaster.virtualdream.virtualDream.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;
	private final ModelMapper mapper;

	private UserDto userToUserDto(User user) {
		return mapper.map(user, UserDto.class);
	}

	private User userDtoToUser(UserDto userDto) {
		return mapper.map(userDto, User.class);
	}

	@GetMapping("/superadmin/users")
	public ResponseEntity<ResponseBody<List<UserDto>>> getAllUsers() {
		var usersList = StreamSupport.stream(userService.getAllUsers().spliterator(), false).toList();
		List<UserDto> users = usersList.stream().map(this::userToUserDto).collect(Collectors.toList());
		return ResponseType.ok("successfullyRequest", users);
	}

	@GetMapping("/superadmin/auth-requests")
	public ResponseEntity<ResponseBody<List<UserDto>>> getUnauthorizedRequests() {
		var userList = StreamSupport.stream(userService.getUnauthorizedUsers().spliterator(), false).toList();
		List<UserDto> users = userList.stream().map(this::userToUserDto).collect(Collectors.toList());
		return ResponseType.ok("successfullyRequest", users);
	}
	
	@PutMapping("/superadmin/auth-requests/{id}")
	public ResponseEntity<ResponseBody<Object>> activeUser(@PathVariable Long id){
		userService.activeUser(id);
		return ResponseType.ok("successfullyActivated");
	}
	
	@DeleteMapping("/superadmin/auth-requests/{id}")
	public ResponseEntity<ResponseBody<Object>> denyUser(@PathVariable Long id){
		userService.denyUser(id);
		return ResponseType.ok("susccessfullyDeny");
	}

	@GetMapping("/private/{id}")
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
