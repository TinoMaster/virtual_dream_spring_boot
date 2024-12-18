package com.tinomaster.virtualdream.virtualDream.dtos.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessResponseDto {
	private long id;
	private String name;
	private String email;
	private String phone;
	private String description;
	private String address;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
