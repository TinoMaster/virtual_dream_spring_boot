package com.tinomaster.virtualdream.virtualdream.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.UserDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.BooleanResponse;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.User;
import com.tinomaster.virtualdream.virtualdream.exceptions.NotFoundException;
import com.tinomaster.virtualdream.virtualdream.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User")
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
    @Hidden
    public ResponseEntity<ResponseBody<List<UserDto>>> getAllUsers() {
        var usersList = StreamSupport.stream(userService.getAllUsers().spliterator(), false).toList();
        List<UserDto> users = usersList.stream().map(this::userToUserDto).collect(Collectors.toList());
        return ResponseType.ok("successfullyRequest", users);
    }

    @Operation(
            description = "Exists any user",
            summary = "This is a summary",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }
    )
    @GetMapping("/public/users/exist")
    public ResponseEntity<ResponseBody<BooleanResponse>> existAnyUser() {
        boolean exist = userService.existAnyUser();
        return ResponseType.ok("successfullyRequest", BooleanResponse.builder().response(exist).build());
    }

    @GetMapping("/private/users/{id}")
    public ResponseEntity<ResponseBody<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = this.userToUserDto(userService.getUserById(id));
        return ResponseType.ok("successfullyRequest", user);
    }

    @GetMapping("/private/users/byEmail/{email}")
    public ResponseEntity<ResponseBody<UserDto>> getUserByEmail(@PathVariable String email) {
        UserDto user = this.userToUserDto(userService.getUserByEmail(email));
        return ResponseType.ok("successfullyRequest", user);
    }

    @GetMapping("/superadmin/auth-requests")
    public ResponseEntity<ResponseBody<List<UserDto>>> getUnauthorizedRequests() {
        var userList = StreamSupport.stream(userService.getUnauthorizedUsers().spliterator(), false).toList();
        List<UserDto> users = userList.stream().map(this::userToUserDto).collect(Collectors.toList());
        return ResponseType.ok("successfullyRequest", users);
    }

    @PutMapping("/superadmin/auth-requests/{id}")
    public ResponseEntity<ResponseBody<Object>> activeUser(@PathVariable Long id) {
        userService.activeUser(id);
        return ResponseType.ok("successfullyActivated");
    }

    @DeleteMapping("/superadmin/auth-requests/{id}")
    public ResponseEntity<ResponseBody<Object>> denyUser(@PathVariable Long id) {
        userService.denyUser(id);
        return ResponseType.ok("susccessfullyDeny");
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
