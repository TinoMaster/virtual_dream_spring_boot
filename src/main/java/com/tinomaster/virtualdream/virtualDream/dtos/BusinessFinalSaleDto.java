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
public class BusinessFinalSaleDto {

	private Long id;
	private BusinessDto business;
	private Float total;
	private Float paid;
	private List<DebtDto> debts;
	private String note;
	private List<EmployeeDto> workers;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
