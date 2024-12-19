package com.tinomaster.virtualdream.virtualDream.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessDto {
	private Long id;
	private String name;
	private String description;
	private String phone;
	private AddressDto address;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
