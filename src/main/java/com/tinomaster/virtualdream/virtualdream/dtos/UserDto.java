package com.tinomaster.virtualdream.virtualdream.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.tinomaster.virtualdream.virtualdream.enums.ERole;

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
	private ERole role;
	private boolean active;
	private LocalDateTime lastLoginAt;
	private List<BusinessDto> businesses;
	private List<BusinessDto> businessesOwned;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
