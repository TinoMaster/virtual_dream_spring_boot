package com.tinomaster.virtualdream.virtualDream.dtos;

import java.time.LocalDateTime;

import com.tinomaster.virtualdream.virtualDream.enums.EUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsumableDto {
	private Long id;
	private String name;
	private Float price;
	private String description;
	private EUnit unit;
	private Float stock;
	private Long business;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
