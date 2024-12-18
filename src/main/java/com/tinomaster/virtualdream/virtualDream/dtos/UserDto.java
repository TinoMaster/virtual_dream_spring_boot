package com.tinomaster.virtualdream.virtualDream.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	private Long id;
	private String name;
	private String email;
	private String password;
	private String role;
	private boolean active;
	private List<BusinessDto> businesses;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
