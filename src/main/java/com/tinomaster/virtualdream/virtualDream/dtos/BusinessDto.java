package com.tinomaster.virtualdream.virtualDream.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessDto {
	private Long id;
	private String name;
	private String email;
	private String description;
	private String phone;
	private AddressDto address;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
