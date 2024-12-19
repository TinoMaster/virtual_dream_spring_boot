package com.tinomaster.virtualdream.virtualDream.dtos.response;

import com.tinomaster.virtualdream.virtualDream.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
	private String token;
	private String refreshToken;
	private ERole role;
}
