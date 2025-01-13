package com.tinomaster.virtualdream.virtualdream.dtos.response;

import com.tinomaster.virtualdream.virtualdream.enums.ERole;

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
	private boolean active;
}
