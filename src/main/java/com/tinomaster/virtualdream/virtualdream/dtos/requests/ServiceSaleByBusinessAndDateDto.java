package com.tinomaster.virtualdream.virtualdream.dtos.requests;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSaleByBusinessAndDateDto {
	private Long businessId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
