package com.tinomaster.virtualdream.virtualdream.dtos;

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
public class ServiceDto {
	private Long id;
	private String name;
	private String description;
	private Float price;
	private Long business;
	private List<ConsumableCostDto> costs;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
