package com.tinomaster.virtualdream.virtualdream.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthOwnerRegisterResponse {
	private boolean success;
	private String message;
}
