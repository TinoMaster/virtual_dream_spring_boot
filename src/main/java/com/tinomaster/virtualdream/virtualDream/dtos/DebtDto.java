package com.tinomaster.virtualdream.virtualDream.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DebtDto {
	private Long id;
	private String name;
	private String description;
	private Float total;
	private Float paid;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
