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
	private String name;
	private BusinessDto business;
	private Float total;
	private Float paid;
	private List<DebtDto> debts;
	private List<MachineDto> machines;
	private String note;
	private List<EmployeeDto> workers;
	private UserDto doneBy;
	private Float found;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
