package com.tinomaster.virtualdream.virtualDream.dtos;

import com.tinomaster.virtualdream.virtualDream.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterDto {
	private String name;
	private String email;
	private String password;
	private ERole role;
	private long businessId;
}
