package com.tinomaster.virtualdream.virtualDream.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
	private long id;
	private String name;
	private String email;
	private String role;
	private boolean active;
	private List<Long> businesses;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
