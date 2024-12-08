package com.tinomaster.virtualdream.virtualDream.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	private long id;
	private String name;
	private String email;
	private String role;
	private boolean active;
	private long businessId;
}
