package com.tinomaster.virtualdream.virtualdream.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessFinalSaleDto {
    private Long id;
    private String name;
    private Long business;
    private Float total;
    private Float paid;
    private List<DebtDto> debts;
    private List<MachineDto> machines;
    private String note;
    private List<EmployeeDto> workers;
    private List<ServiceSaleDto> servicesSales;
    private List<CardDto> cards;
    private List<MachineStateDto> machineStates;
    private Long doneBy;
    private Float fund;
    private Float percentSalary;
    private Integer fixedSalary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
