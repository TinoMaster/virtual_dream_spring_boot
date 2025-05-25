package com.tinomaster.virtualdream.virtualdream.statistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeSalesResumeDto {
    private Float totalSales;
    private Float totalServices;
    private Float totalDebts;
    private Float totalSalaries;
}
