package com.tinomaster.virtualdream.virtualdream.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceSaleDto {
	private Long id;
	private Integer quantity;
	private ServiceDto service;
	private EmployeeDto employee;
	private Long businessFinalSale;
	private Long business;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
