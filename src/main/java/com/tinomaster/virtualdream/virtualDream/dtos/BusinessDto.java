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
public class BusinessDto {
	private Long id;
	private String name;
	private String description;
	private String phone;
	private List<Long> users;
	private AddressDto address;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
